package com.ash.trading.oms.order.statemachine.handlers

import com.ash.trading.oms.model.OrderQuantity
import com.ash.trading.oms.order.statemachine.OmsOrderState
import com.ash.trading.oms.order.statemachine.IOmsOrderState
import com.ash.trading.oms.order.statemachine.events.OmsOrderEvent
import com.ash.trading.oms.order.statemachine.events.OrderCancelledEvent
import com.ash.trading.oms.order.statemachine.events.TraderExecutedEvent
import com.ash.trading.oms.order.statemachine.events.TraderWorkingEvent

object OmsOrderCancelledStateHandler {

    fun <T> handleEvent(data: OrderQuantity, event: OmsOrderEvent<T>): Pair<OrderQuantity, IOmsOrderState> {
        return when (event) {
            is TraderExecutedEvent -> data to OmsOrderState.EXECUTED
            is OrderCancelledEvent -> data to OmsOrderState.CANCELLED
            is TraderWorkingEvent -> data to OmsOrderState.WORKED
        }
    }

}