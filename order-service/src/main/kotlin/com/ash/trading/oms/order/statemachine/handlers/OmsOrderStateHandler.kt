package com.ash.trading.oms.order.statemachine.handlers

import com.ash.trading.oms.model.OrderQuantity
import com.ash.trading.oms.order.statemachine.OrderQuantityState
import com.ash.trading.oms.order.statemachine.events.OmsOrderEvent

sealed interface OmsOrderStateHandler {

    fun handleEvent(data: OrderQuantity, event: OmsOrderEvent): OrderQuantityState

}