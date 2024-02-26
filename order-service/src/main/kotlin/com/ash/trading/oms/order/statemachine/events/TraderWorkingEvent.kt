package com.ash.trading.oms.order.statemachine.events

import com.ash.trading.oms.model.WorkedQuantity
import com.ash.trading.oms.user.TraderId

data class TraderWorkingEvent(override val payload: TraderWorkingPayload): OmsOrderEvent<TraderWorkingPayload>
data class TraderWorkingPayload(val traderId: TraderId, val workedQuantity: WorkedQuantity)