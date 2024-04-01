package com.ash.trading.oms.tradeorder.statemachine

import com.ash.trading.oms.model.TradeOrderQuantities
import com.ash.trading.oms.tradeorder.statemachine.event.OmsTradeOrderEvent
import com.ash.trading.oms.tradeorder.statemachine.handlers.OmsTradeOrderExecutedStateHandler
import com.ash.trading.oms.tradeorder.statemachine.handlers.OmsTradeOrderNewStateHandler
import com.ash.trading.oms.tradeorder.statemachine.handlers.OmsTradeOrderPartiallyExecutedStateHandler
import com.ash.trading.oms.tradeorder.statemachine.handlers.OmsTradeOrderTerminalStateHandler

enum class OmsTradeOrderState {

    NEW {
        override fun handleEvent(data: TradeOrderQuantities, event: OmsTradeOrderEvent) = OmsTradeOrderNewStateHandler.handleEvent(data, event)
    },
    PARTIALLY_EXECUTED {
        override fun handleEvent(data: TradeOrderQuantities, event: OmsTradeOrderEvent) = OmsTradeOrderPartiallyExecutedStateHandler.handleEvent(data, event)
    },
    EXECUTED {
        override fun handleEvent(data: TradeOrderQuantities, event: OmsTradeOrderEvent) = OmsTradeOrderExecutedStateHandler.handleEvent(data, event)
    },
    COMPLETED {
        private val handler = OmsTradeOrderTerminalStateHandler(COMPLETED)
        override fun handleEvent(data: TradeOrderQuantities, event: OmsTradeOrderEvent) = handler.handleEvent(data, event)
    },
    CANCELLED {
        private val handler = OmsTradeOrderTerminalStateHandler(CANCELLED)
        override fun handleEvent(data: TradeOrderQuantities, event: OmsTradeOrderEvent) = handler.handleEvent(data, event)
    },
    DELETED {
        private val handler = OmsTradeOrderTerminalStateHandler(DELETED)
        override fun handleEvent(data: TradeOrderQuantities, event: OmsTradeOrderEvent) = handler.handleEvent(data, event)
    };

    abstract fun handleEvent(data: TradeOrderQuantities, event: OmsTradeOrderEvent): TradeOrderQuantitiesState

}

typealias TradeOrderQuantitiesState = Pair<TradeOrderQuantities, OmsTradeOrderState>