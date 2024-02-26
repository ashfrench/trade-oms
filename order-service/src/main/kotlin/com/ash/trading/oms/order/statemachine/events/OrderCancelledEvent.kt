package com.ash.trading.oms.order.statemachine.events

import com.ash.trading.oms.model.CancelledQuantity
import java.time.LocalDateTime

data class OrderCancelledEvent(override val payload: OrderCancelledPayload): OmsOrderEvent<OrderCancelledPayload>
data class OrderCancelledPayload(val cancelledTime: LocalDateTime)