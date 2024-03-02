package com.ash.trading.oms.tradeorder.statemachine

import com.ash.trading.oms.model.OrderQuantity
import com.ash.trading.oms.tradeorder.statemachine.event.OmsTradeOrderEvent
import com.ash.trading.oms.tradeorder.statemachine.handlers.*

enum class OmsTradeOrderState {

    NEW {
        override fun <T> handleEvent(data: OrderQuantity, event: OmsTradeOrderEvent<T>) = OmsTradeOrderNewStateHandler.handleEvent(data, event)
    },
    PARTIALLY_EXECUTED {
        override fun <T> handleEvent(data: OrderQuantity, event: OmsTradeOrderEvent<T>) = OmsTradeOrderPartiallyExecutedStateHandler.handleEvent(data, event)
    },
    EXECUTED {
        private val handler = OmsTradeOrderTerminalStateHandler(EXECUTED)
        override fun <T> handleEvent(data: OrderQuantity, event: OmsTradeOrderEvent<T>) = handler.handleEvent(data, event)
    },
    CANCELLED {
        private val handler = OmsTradeOrderTerminalStateHandler(CANCELLED)
        override fun <T> handleEvent(data: OrderQuantity, event: OmsTradeOrderEvent<T>) = handler.handleEvent(data, event)
    },
    DELETED {
        private val handler = OmsTradeOrderTerminalStateHandler(DELETED)
        override fun <T> handleEvent(data: OrderQuantity, event: OmsTradeOrderEvent<T>) = handler.handleEvent(data, event)
    };

    abstract fun <T> handleEvent(data: OrderQuantity, event: OmsTradeOrderEvent<T>): Pair<OrderQuantity, OmsTradeOrderState>

}