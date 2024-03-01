package com.ash.trading.oms.tradeorder.statemachine

import com.ash.trading.oms.model.OrderQuantity
import com.ash.trading.oms.tradeorder.statemachine.event.OmsTradeOrderEvent
import com.ash.trading.oms.tradeorder.statemachine.handlers.OmsTradeOrderCancelledStateHandler

enum class OmsTradeOrderState {

    NEW {
        override fun <T> handleEvent(
            data: OrderQuantity,
            event: OmsTradeOrderEvent<T>
        ): Pair<OrderQuantity, OmsTradeOrderState> {
            TODO("Not yet implemented")
        }
    },
    PARTIALLY_EXECUTED {
        override fun <T> handleEvent(
            data: OrderQuantity,
            event: OmsTradeOrderEvent<T>
        ): Pair<OrderQuantity, OmsTradeOrderState> {
            TODO("Not yet implemented")
        }
    },
    EXECUTED {
        override fun <T> handleEvent(
            data: OrderQuantity,
            event: OmsTradeOrderEvent<T>
        ): Pair<OrderQuantity, OmsTradeOrderState> {
            TODO("Not yet implemented")
        }
    },
    CANCELLED {
        override fun <T> handleEvent(data: OrderQuantity, event: OmsTradeOrderEvent<T>) = OmsTradeOrderCancelledStateHandler.handleEvent(data, event)
    },
    DELETED {
        override fun <T> handleEvent(
            data: OrderQuantity,
            event: OmsTradeOrderEvent<T>
        ): Pair<OrderQuantity, OmsTradeOrderState> {
            TODO("Not yet implemented")
        }
    };

    abstract fun <T> handleEvent(data: OrderQuantity, event: OmsTradeOrderEvent<T>): Pair<OrderQuantity, OmsTradeOrderState>

}