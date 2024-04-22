package com.ash.trading.oms.model

data class Order(
    val orderId: OrderId = newOrderId(),
    val orderQuantity: OrderQuantity,
    val instrument: Instrument,
    val side: TradeSide,
    val orderState: OrderState = OrderState.NEW,
    val tradeOrders: List<TradeOrderId> = emptyList()
)

