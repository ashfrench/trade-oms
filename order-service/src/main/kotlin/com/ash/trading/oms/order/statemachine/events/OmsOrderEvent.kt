package com.ash.trading.oms.order.statemachine.events

import com.ash.trading.oms.statemachine.Event
sealed interface OmsOrderEvent<T>: Event<T>
