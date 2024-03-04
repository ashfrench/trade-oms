package com.ash.trading.oms.model

import java.math.BigDecimal

data class TradeOrderQuantities(
    val orderQuantities: Map<OrderId, WorkedQuantity>,
    val tradeQuantities: Map<TradeId, TradeQuantity> = emptyMap(),
    val cancelledQuantity: CancelledQuantity = BigDecimal.ZERO,
) {
    val totalQuantity: TotalQuantity = orderQuantities.values.sumOf { it }
    val executedQuantity: ExecutedQuantity = tradeQuantities.values.sumOf { it }
    val usedQuantity: UsedQuantity = executedQuantity + cancelledQuantity
    val openQuantity: OpenQuantity = totalQuantity - usedQuantity
}