package com.ash.trading.oms.model

import java.util.*

data class Order(
    val orderId: OrderId,
    val orderQuantity: OrderQuantity,
    val instrument: Instrument,
    val side: TradeSide
)

typealias OrderId = UUID