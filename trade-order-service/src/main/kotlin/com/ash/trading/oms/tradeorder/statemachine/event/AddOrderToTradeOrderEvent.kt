package com.ash.trading.oms.tradeorder.statemachine.event

import com.ash.trading.oms.model.OrderId
import com.ash.trading.oms.model.WorkedQuantity

data class AddOrderToTradeOrderEvent(
    val orderId: OrderId,
    val workedQuantity: WorkedQuantity
): OmsTradeOrderEvent
