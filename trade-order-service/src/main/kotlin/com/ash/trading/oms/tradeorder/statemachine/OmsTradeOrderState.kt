package com.ash.trading.oms.tradeorder.statemachine

import com.ash.trading.oms.model.TradeOrderQuantities
import com.ash.trading.oms.model.compareTo
import com.ash.trading.oms.tradeorder.statemachine.event.OmsTradeOrderEvent
import com.ash.trading.oms.tradeorder.statemachine.handlers.OmsTradeOrderExecutedStateHandler
import com.ash.trading.oms.tradeorder.statemachine.handlers.OmsTradeOrderNewStateHandler
import com.ash.trading.oms.tradeorder.statemachine.handlers.OmsTradeOrderPartiallyExecutedStateHandler
import com.ash.trading.oms.tradeorder.statemachine.handlers.OmsTradeOrderTerminalStateHandler
import java.math.BigDecimal

enum class OmsTradeOrderState {

    NEW {
        override fun handleEvent(data: TradeOrderQuantities, event: OmsTradeOrderEvent) = OmsTradeOrderNewStateHandler.handleEvent(data, event)
        override fun isValid(data: TradeOrderQuantities): Boolean = data.usedQuantity == BigDecimal.ZERO
        override fun validate(data: TradeOrderQuantities) = check (data in NEW) { "$NEW Data should have ZERO used quantity" }

    },
    PARTIALLY_EXECUTED {
        override fun handleEvent(data: TradeOrderQuantities, event: OmsTradeOrderEvent) = OmsTradeOrderPartiallyExecutedStateHandler.handleEvent(data, event)
        override fun isValid(data: TradeOrderQuantities): Boolean = data.executedQuantity > BigDecimal.ZERO && data.openQuantity > BigDecimal.ZERO
        override fun validate(data: TradeOrderQuantities) = check(data in PARTIALLY_EXECUTED) {
            "$PARTIALLY_EXECUTED Data should have a positive used quantity and positive open quantity"
        }
    },
    EXECUTED {
        override fun handleEvent(data: TradeOrderQuantities, event: OmsTradeOrderEvent) = OmsTradeOrderExecutedStateHandler.handleEvent(data, event)
        override fun isValid(data: TradeOrderQuantities): Boolean = data.openQuantity == BigDecimal.ZERO && data.executedQuantity > BigDecimal.ZERO
        override fun validate(data: TradeOrderQuantities) = check (data in EXECUTED) {
            "$EXECUTED Data should have ZERO open quantity and executed quantity GREATER than ZERO"
        }
    },
    COMPLETED {
        private val handler = OmsTradeOrderTerminalStateHandler(COMPLETED)
        override fun handleEvent(data: TradeOrderQuantities, event: OmsTradeOrderEvent) = handler.handleEvent(data, event)
        override fun isValid(data: TradeOrderQuantities): Boolean = data.completedTime != null
        override fun validate(data: TradeOrderQuantities) = check (data in COMPLETED) {
            "$COMPLETED Data should have ZERO open quantity and Completed Time NOT NULL"
        }
    },
    CANCELLED {
        private val handler = OmsTradeOrderTerminalStateHandler(CANCELLED)
        override fun handleEvent(data: TradeOrderQuantities, event: OmsTradeOrderEvent) = handler.handleEvent(data, event)
        override fun isValid(data: TradeOrderQuantities): Boolean = data.cancelledQuantity > BigDecimal.ZERO
        override fun validate(data: TradeOrderQuantities) = check (data in CANCELLED) { "$CANCELLED Data should have ZERO open quantity and cancelled time not null" }
    },
    DELETED {
        private val handler = OmsTradeOrderTerminalStateHandler(DELETED)
        override fun handleEvent(data: TradeOrderQuantities, event: OmsTradeOrderEvent) = handler.handleEvent(data, event)
        override fun isValid(data: TradeOrderQuantities): Boolean = TODO()
        override fun validate(data: TradeOrderQuantities) = TODO()
    },
    ERROR {
        override fun handleEvent(data: TradeOrderQuantities, event: OmsTradeOrderEvent): TradeOrderQuantitiesState = TODO()
        override fun isValid(data: TradeOrderQuantities): Boolean = TODO()
        override fun validate(data: TradeOrderQuantities) = TODO()
    };

    abstract fun handleEvent(data: TradeOrderQuantities, event: OmsTradeOrderEvent): TradeOrderQuantitiesState
    abstract fun isValid(data: TradeOrderQuantities): Boolean
    abstract fun validate(data: TradeOrderQuantities)

}

operator fun OmsTradeOrderState.contains(data: TradeOrderQuantities): Boolean = isValid(data)
typealias TradeOrderQuantitiesState = Pair<TradeOrderQuantities, OmsTradeOrderState>