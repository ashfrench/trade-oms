package com.ash.trading.oms.model

import java.util.UUID

data class Order(
    val orderId: OrderId = newOrderId(),
    val orderQuantity: OrderQuantity,
    val instrument: Instrument,
    val side: TradeSide,
    val orderState: OrderState = OrderState.NEW,
    val tradeOrders: List<TradeOrderId> = emptyList()
)

typealias OrderId = UUID

fun newOrderId(): OrderId = UUID.randomUUID()