package com.ash.trading.oms.order.statemachine

import com.ash.trading.oms.model.OrderQuantity
import com.ash.trading.oms.order.statemachine.events.OmsOrderEvent
import com.ash.trading.oms.order.statemachine.handlers.*

enum class OmsOrderState: IOmsOrderState {


    NEW {
        override fun <T> handleEvent(data: OrderQuantity, event: OmsOrderEvent<T>): Pair<OrderQuantity, IOmsOrderState> = OmsOrderNewStateHandler.handleEvent(data, event)
    },
    WORKED {
        override fun <T> handleEvent(data: OrderQuantity, event: OmsOrderEvent<T>): Pair<OrderQuantity, IOmsOrderState> = OmsOrderWorkedStateHandler.handleEvent(data, event)
    },
    EXECUTED {
        override fun <T> handleEvent(data: OrderQuantity, event: OmsOrderEvent<T>): Pair<OrderQuantity, IOmsOrderState> = OmsOrderExecutedStateHandler.handleEvent(data, event)
    },
    PARTIALLY_EXECUTED {
        override fun <T> handleEvent(data: OrderQuantity, event: OmsOrderEvent<T>): Pair<OrderQuantity, IOmsOrderState> = OmsOrderPartiallyExecutedStateHandler.handleEvent(data, event)
    },
    CANCELLED {
        override fun <T> handleEvent(data: OrderQuantity, event: OmsOrderEvent<T>): Pair<OrderQuantity, IOmsOrderState> = OmsOrderCancelledStateHandler.handleEvent(data, event)
    },
}