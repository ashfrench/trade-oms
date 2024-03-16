package com.ash.trading.oms.tradeorder.statemachine.event

import com.ash.trading.oms.model.ExecutedQuantity
import com.ash.trading.oms.model.TradeId

data class UpdateTradeForTradeOrderEvent(val tradeId: TradeId, val executedQuantity: ExecutedQuantity): OmsTradeOrderEvent