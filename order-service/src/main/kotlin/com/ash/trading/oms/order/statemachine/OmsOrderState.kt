package com.ash.trading.oms.order.statemachine

import com.ash.trading.oms.model.OrderQuantity
import com.ash.trading.oms.order.statemachine.events.OmsOrderEvent
import com.ash.trading.oms.order.statemachine.handlers.OmsOrderNewStateHandler

enum class OmsOrderState: IOmsOrderState {


    NEW {
        override fun <T> handleEvent(data: OrderQuantity, event: OmsOrderEvent<T>): Pair<OrderQuantity, IOmsOrderState> = OmsOrderNewStateHandler.handleEvent(data, event)
    },
    WORKED {
        override fun <T> handleEvent(data: OrderQuantity, event: OmsOrderEvent<T>): Pair<OrderQuantity, IOmsOrderState> {
            TODO("Not yet implemented")
        }
    },
    EXECUTED {
        override fun <T> handleEvent(data: OrderQuantity, event: OmsOrderEvent<T>): Pair<OrderQuantity, IOmsOrderState> {
            TODO("Not yet implemented")
        }
    },
    PARTIALLY_EXECUTED {
        override fun <T> handleEvent(data: OrderQuantity, event: OmsOrderEvent<T>): Pair<OrderQuantity, IOmsOrderState> {
            TODO("Not yet implemented")
        }
    },
    CANCELLED {
        override fun <T> handleEvent(data: OrderQuantity, event: OmsOrderEvent<T>): Pair<OrderQuantity, IOmsOrderState> {
            TODO("Not yet implemented")
        }
    },
}