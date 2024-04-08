package com.ash.trading.oms.tradeorder.statemachine.event

import com.ash.trading.oms.model.OrderId

data class RemoveOrderFromTradeOrderEvent(
    val orderId: OrderId
): OmsTradeOrderEvent
