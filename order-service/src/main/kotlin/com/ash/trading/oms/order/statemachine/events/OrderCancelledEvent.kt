package com.ash.trading.oms.order.statemachine.events

import java.time.LocalDateTime

data class OrderCancelledEvent(val cancelledTime: LocalDateTime): OmsOrderEvent