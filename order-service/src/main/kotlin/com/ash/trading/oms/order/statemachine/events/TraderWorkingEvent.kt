package com.ash.trading.oms.order.statemachine.events

import com.ash.trading.oms.model.TraderId
import com.ash.trading.oms.model.WorkedQuantity

data class TraderWorkingEvent(val traderId: TraderId, val workedQuantity: WorkedQuantity): OmsOrderEvent