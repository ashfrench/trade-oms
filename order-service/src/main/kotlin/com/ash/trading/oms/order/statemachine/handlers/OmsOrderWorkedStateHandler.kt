package com.ash.trading.oms.order.statemachine.handlers

import com.ash.trading.oms.model.CancelledQuantity
import com.ash.trading.oms.model.OrderQuantity
import com.ash.trading.oms.order.statemachine.OmsOrderState
import com.ash.trading.oms.order.statemachine.OrderQuantityState
import com.ash.trading.oms.order.statemachine.contains
import com.ash.trading.oms.order.statemachine.events.OmsOrderEvent
import com.ash.trading.oms.order.statemachine.events.OrderCancelledEvent
import com.ash.trading.oms.order.statemachine.events.TraderExecutedEvent
import com.ash.trading.oms.order.statemachine.events.TraderWorkingEvent
import org.slf4j.LoggerFactory
import java.math.BigDecimal

object OmsOrderWorkedStateHandler {

    private val logger = LoggerFactory.getLogger(OmsOrderWorkedStateHandler::class.java)

    fun handleEvent(data: OrderQuantity, event: OmsOrderEvent): OrderQuantityState {
        check (data in OmsOrderState.WORKED) {
            "${OmsOrderState.WORKED} Data should have worked quantity greater than ZERO and open quantity greater than ZERO"
        }

        return try {
            when (event) {
                is TraderExecutedEvent -> handleTradeExecution(data, event)
                is OrderCancelledEvent -> handleOrderCancellation(data, event)
                is TraderWorkingEvent -> handleTraderWorkingEvent(data, event)
            }
        } catch (e: Exception) {
            logger.error("Error Handling Event Type [${event.javaClass.simpleName}] from ${OmsOrderState.WORKED} state", e)
            data to OmsOrderState.WORKED
        }
    }

    private fun handleTraderWorkingEvent(data: OrderQuantity, event: TraderWorkingEvent): OrderQuantityState {
        val updatedData = data.copy(
            workedQuantity = data.workedQuantity + event.workedQuantity
        )
        return updatedData to OmsOrderState.WORKED
    }

    private fun handleTradeExecution(data: OrderQuantity, event: TraderExecutedEvent): OrderQuantityState {
        val updatedData = data.copy(
            executedQuantity = event.executedQuantity,
            workedQuantity = data.workedQuantity - event.executedQuantity
        )

        return if (updatedData.executedQuantity == updatedData.totalQuantity) {
            updatedData to OmsOrderState.EXECUTED
        } else {
            updatedData to OmsOrderState.PARTIALLY_EXECUTED
        }
    }

    private fun handleOrderCancellation(data: OrderQuantity, event: OrderCancelledEvent): OrderQuantityState {
        val updatedData = data.copy(
            cancelledQuantity = CancelledQuantity(data.workedQuantity + data.openQuantity, event.cancelledTime),
            workedQuantity = BigDecimal.ZERO
        )
        return updatedData to OmsOrderState.CANCELLED
    }

}