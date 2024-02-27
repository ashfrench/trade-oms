package com.ash.trading.oms.order.statemachine

import com.ash.trading.oms.model.OrderQuantity
import com.ash.trading.oms.order.statemachine.events.OmsOrderEvent
import com.ash.trading.oms.order.statemachine.handlers.*

enum class OmsOrderState {


    NEW {
        override fun <T> handleEvent(data: OrderQuantity, event: OmsOrderEvent<T>): Pair<OrderQuantity, OmsOrderState> = OmsOrderNewStateHandler.handleEvent(data, event)
    },
    WORKED {
        override fun <T> handleEvent(data: OrderQuantity, event: OmsOrderEvent<T>): Pair<OrderQuantity, OmsOrderState> = OmsOrderWorkedStateHandler.handleEvent(data, event)
    },
    EXECUTED {
        override fun <T> handleEvent(data: OrderQuantity, event: OmsOrderEvent<T>): Pair<OrderQuantity, OmsOrderState> = OmsOrderExecutedStateHandler.handleEvent(data, event)
    },
    PARTIALLY_EXECUTED {
        override fun <T> handleEvent(data: OrderQuantity, event: OmsOrderEvent<T>): Pair<OrderQuantity, OmsOrderState> = OmsOrderPartiallyExecutedStateHandler.handleEvent(data, event)
    },
    CANCELLED {
        override fun <T> handleEvent(data: OrderQuantity, event: OmsOrderEvent<T>): Pair<OrderQuantity, OmsOrderState> = OmsOrderCancelledStateHandler.handleEvent(data, event)
    };

    abstract fun <T> handleEvent(data: OrderQuantity, event: OmsOrderEvent<T>): Pair<OrderQuantity, Any>
}