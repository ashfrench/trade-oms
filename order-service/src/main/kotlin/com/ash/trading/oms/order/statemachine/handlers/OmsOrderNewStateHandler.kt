package com.ash.trading.oms.order.statemachine.handlers

import com.ash.trading.oms.model.OrderQuantity
import com.ash.trading.oms.order.statemachine.IOmsOrderState
import com.ash.trading.oms.order.statemachine.OmsOrderState
import com.ash.trading.oms.order.statemachine.events.OmsOrderEvent
import com.ash.trading.oms.order.statemachine.events.OrderCancelledEvent
import com.ash.trading.oms.order.statemachine.events.TraderWorkingEvent
import org.slf4j.LoggerFactory

object OmsOrderNewStateHandler {

    private val logger = LoggerFactory.getLogger(OmsOrderNewStateHandler::class.java)

    fun <T> handleEvent(data: OrderQuantity, event: OmsOrderEvent<T>): Pair<OrderQuantity, IOmsOrderState> {
        return when (event) {
            is OrderCancelledEvent -> handleOrderCancelledEvent(data)
            is TraderWorkingEvent -> handleTraderWorkingEvent(data, event)
            else -> handleUnplannedEvent(data, event)
        }
    }

    private fun handleOrderCancelledEvent(data: OrderQuantity): Pair<OrderQuantity, IOmsOrderState> {
        val updatedData = data.copy(cancelledQuantity = data.totalQuantity)
        return updatedData to OmsOrderState.CANCELLED
    }

    private fun handleTraderWorkingEvent(data: OrderQuantity, event: TraderWorkingEvent): Pair<OrderQuantity, IOmsOrderState> {
        val updatedData = data.copy(workedQuantity = event.payload.workedQuantity)
        return updatedData to OmsOrderState.WORKED
    }

    private fun <T> handleUnplannedEvent(data: OrderQuantity, event: OmsOrderEvent<T>): Pair<OrderQuantity, IOmsOrderState> {
        logger.error("Invalid Event Type [${event.javaClass.simpleName}] from NEW state")
        return data to OmsOrderState.NEW
    }

}