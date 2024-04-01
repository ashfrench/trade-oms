package com.ash.trading.oms.order.statemachine

import com.ash.trading.oms.model.OrderQuantity
import com.ash.trading.oms.order.statemachine.events.OmsOrderEvent
import com.ash.trading.oms.order.statemachine.handlers.*

enum class OmsOrderState {


    NEW {
        override fun handleEvent(data: OrderQuantity, event: OmsOrderEvent): OrderQuantityState = OmsOrderNewStateHandler.handleEvent(data, event)
    },
    WORKED {
        override fun handleEvent(data: OrderQuantity, event: OmsOrderEvent): OrderQuantityState = OmsOrderWorkedStateHandler.handleEvent(data, event)
    },
    EXECUTED {
        override fun handleEvent(data: OrderQuantity, event: OmsOrderEvent): OrderQuantityState = OmsOrderExecutedStateHandler.handleEvent(data, event)
    },
    PARTIALLY_EXECUTED {
        override fun handleEvent(data: OrderQuantity, event: OmsOrderEvent): OrderQuantityState = OmsOrderPartiallyExecutedStateHandler.handleEvent(data, event)
    },
    CANCELLED {
        override fun handleEvent(data: OrderQuantity, event: OmsOrderEvent): OrderQuantityState = OmsOrderCancelledStateHandler.handleEvent(data, event)
    };

    abstract fun handleEvent(data: OrderQuantity, event: OmsOrderEvent): OrderQuantityState
}

typealias OrderQuantityState = Pair<OrderQuantity, OmsOrderState>