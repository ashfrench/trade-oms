package com.ash.trading.oms.order.statemachine.handlers

import com.ash.trading.oms.model.CancelledQuantity
import com.ash.trading.oms.model.OrderQuantity
import com.ash.trading.oms.order.statemachine.OmsOrderState
import com.ash.trading.oms.order.statemachine.OrderQuantityState
import com.ash.trading.oms.order.statemachine.events.OmsOrderEvent
import com.ash.trading.oms.order.statemachine.events.OrderCancelledEvent
import com.ash.trading.oms.order.statemachine.events.TraderWorkingEvent
import org.slf4j.LoggerFactory
import java.math.BigDecimal

object OmsOrderNewStateHandler {

    private val logger = LoggerFactory.getLogger(OmsOrderNewStateHandler::class.java)

    fun handleEvent(data: OrderQuantity, event: OmsOrderEvent): OrderQuantityState {
        check (data.usedQuantity == BigDecimal.ZERO) {
            "${OmsOrderState.NEW} Data should have ZERO used quantity"
        }


        return try {
            when (event) {
                is OrderCancelledEvent -> handleOrderCancelledEvent(data, event)
                is TraderWorkingEvent -> handleTraderWorkingEvent(data, event)
                else -> handleUnplannedEvent(data, event)
            }
        } catch (e: Exception) {
            logger.error("Error Handling Event Type [${event.javaClass.simpleName}] from ${OmsOrderState.NEW} state", e)
            data to OmsOrderState.NEW
        }
    }

    private fun handleOrderCancelledEvent(data: OrderQuantity, event: OrderCancelledEvent): OrderQuantityState {
        val updatedData = data.copy(cancelledQuantity = CancelledQuantity(data.totalQuantity, event.cancelledTime))
        return updatedData to OmsOrderState.CANCELLED
    }

    private fun handleTraderWorkingEvent(data: OrderQuantity, event: TraderWorkingEvent): OrderQuantityState {
        val updatedData = data.copy(workedQuantity = event.workedQuantity)
        return updatedData to OmsOrderState.WORKED
    }

    private fun handleUnplannedEvent(data: OrderQuantity, event: OmsOrderEvent): OrderQuantityState {
        logger.warn("Invalid Event Type [${event.javaClass.simpleName}] from ${OmsOrderState.NEW} state")
        return data to OmsOrderState.NEW
    }

}