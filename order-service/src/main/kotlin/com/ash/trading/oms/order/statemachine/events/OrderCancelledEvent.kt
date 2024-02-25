package com.ash.trading.oms.order.statemachine.events

import java.time.LocalDateTime

data class OrderCancelledEvent(override val payload: OrderCancelledPayload): OmsOrderEvent<OrderCancelledPayload>
data class OrderCancelledPayload(val cancelledTime: LocalDateTime)