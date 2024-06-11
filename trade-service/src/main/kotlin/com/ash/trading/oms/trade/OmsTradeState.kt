package com.ash.trading.oms.trade

import com.ash.trading.oms.model.TradeQuantity
import com.ash.trading.oms.trade.events.OmsTradeEvent
import com.ash.trading.oms.trade.events.OmsTradeNewStateHandler
import com.ash.trading.oms.trade.events.TradeStateEventHandler
import java.math.BigDecimal

enum class OmsTradeState {

    NEW {
        override fun isValid(data: TradeQuantity): Boolean = data.usedQuantity == BigDecimal.ZERO
        override fun validate(data: TradeQuantity) = check (data in NEW) { "$NEW Data should have ZERO used quantity" }
        override val handler: TradeStateEventHandler = OmsTradeNewStateHandler
    },
    SENT_TO_EMS {
        override fun isValid(data: TradeQuantity): Boolean = data.workedQuantity > BigDecimal.ZERO
        override fun validate(data: TradeQuantity) = check(data in SENT_TO_EMS) { "$SENT_TO_EMS Data should have worked quantity" }
    },
    CANCELLED{
        override fun isValid(data: TradeQuantity): Boolean = TODO()
        override fun validate(data: TradeQuantity) = TODO()
    },
    EXECUTED {
        override fun isValid(data: TradeQuantity): Boolean = TODO()
        override fun validate(data: TradeQuantity) = TODO()
    },
    COMPLETED {
        override fun isValid(data: TradeQuantity): Boolean = TODO()
        override fun validate(data: TradeQuantity) = TODO()
    },
    ERROR {
        override fun isValid(data: TradeQuantity): Boolean = TODO()
        override fun validate(data: TradeQuantity) = TODO()
    };

    fun handleEvent(data: TradeQuantity, event: OmsTradeEvent): TradeQuantityState {
        validate(data)
        return handler.handleEvent(data, event)
    }

    abstract fun isValid(data: TradeQuantity): Boolean
    abstract fun validate(data: TradeQuantity)
    open val handler: TradeStateEventHandler = TODO()

}

operator fun OmsTradeState.contains(data: TradeQuantity): Boolean = isValid(data)
typealias TradeQuantityState = Pair<TradeQuantity, OmsTradeState>