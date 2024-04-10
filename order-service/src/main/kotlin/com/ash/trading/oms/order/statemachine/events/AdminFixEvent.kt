package com.ash.trading.oms.order.statemachine.events

import com.ash.trading.oms.model.OrderQuantity
import com.ash.trading.oms.model.UserId
import com.ash.trading.oms.order.statemachine.OmsOrderState

data class AdminFixEvent(val userId: UserId, val data: OrderQuantity, val newState: OmsOrderState): OmsOrderEvent