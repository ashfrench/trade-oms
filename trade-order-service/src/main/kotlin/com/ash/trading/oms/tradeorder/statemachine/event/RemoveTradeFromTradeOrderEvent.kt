package com.ash.trading.oms.tradeorder.statemachine.event

import com.ash.trading.oms.model.TradeId

data class RemoveTradeFromTradeOrderEvent(
    val tradeId: TradeId
): OmsTradeOrderEvent
