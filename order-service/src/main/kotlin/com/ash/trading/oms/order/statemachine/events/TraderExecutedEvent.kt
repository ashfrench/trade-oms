package com.ash.trading.oms.order.statemachine.events

import com.ash.trading.oms.model.ExecutedQuantity
import com.ash.trading.oms.user.TraderId

data class TraderExecutedEvent(override val payload: TraderExecutedPayload): OmsOrderEvent<TraderExecutedPayload>
data class TraderExecutedPayload(val traderId: TraderId, val executedQuantity: ExecutedQuantity)