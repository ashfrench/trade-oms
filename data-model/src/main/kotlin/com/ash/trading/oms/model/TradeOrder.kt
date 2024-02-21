package com.ash.trading.oms.model

import java.math.BigDecimal
import java.util.UUID

data class TradeOrder(
    val tradeOrderId: TradeOrderId,
    val orderQuantities: Map<OrderId, OrderQuantity>,
    val tradeQuantities: Map<TradeId, TradeQuantity>
)

typealias TradeOrderId = UUID