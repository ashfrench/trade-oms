package com.ash.trading.oms.order.statemachine.events

import com.ash.trading.oms.model.WorkedQuantity
import com.ash.trading.oms.user.TraderId

data class TraderWorkingEvent(val traderId: TraderId, val workedQuantity: WorkedQuantity): OmsOrderEvent