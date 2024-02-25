package com.ash.trading.oms.order.statemachine

import com.ash.trading.oms.order.statemachine.events.OmsOrderEvent
import com.ash.trading.oms.order.statemachine.handlers.OmsOrderNewStateHandler

enum class OmsOrderState: OmsOrderStateInterface {
    NEW {
        private val handler = OmsOrderNewStateHandler()
        override fun <T> handleEvent(event: OmsOrderEvent<T>): OmsOrderStateInterface = handler.handleEvent(event)
    },
    WORKED {
        override fun <T> handleEvent(event: OmsOrderEvent<T>): OmsOrderStateInterface {
            TODO("Not yet implemented")
        }
    },
    EXECUTED {
        override fun <T> handleEvent(event: OmsOrderEvent<T>): OmsOrderStateInterface {
            TODO("Not yet implemented")
        }
    },
    PARTIALLY_EXECUTED {
        override fun <T> handleEvent(event: OmsOrderEvent<T>): OmsOrderStateInterface {
            TODO("Not yet implemented")
        }
    },
    CANCELLED {
        override fun <T> handleEvent(event: OmsOrderEvent<T>): OmsOrderStateInterface {
            TODO("Not yet implemented")
        }
    },
}