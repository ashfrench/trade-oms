package com.ash.trading.oms.tradeorder.statemachine.event

import com.ash.trading.oms.model.ExecutedQuantity
import com.ash.trading.oms.model.TradeId

data class AddTradeToTradeOrderEvent(override val payload: AddTradePayload): OmsTradeOrderEvent<AddTradePayload>
data class AddTradePayload(val tradeId: TradeId, val executedQuantity: ExecutedQuantity)