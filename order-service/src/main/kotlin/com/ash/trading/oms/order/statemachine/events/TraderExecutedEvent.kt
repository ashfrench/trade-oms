package com.ash.trading.oms.order.statemachine.events

import com.ash.trading.oms.model.ExecutedQuantity
import com.ash.trading.oms.model.user.TraderId

data class TraderExecutedEvent(val traderId: TraderId, val executedQuantity: ExecutedQuantity): OmsOrderEvent