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

    init {
        validate()
    }

    private fun validate() {
        check(orderQuantities.all { it.value > BigDecimal.ZERO }) { "All Order Quantities must be greater than 0"}
        check(tradeQuantities.all { it.value > BigDecimal.ZERO }) { "All Trade Quantities must be greater than 0"}
        check(cancelledQuantity >= BigDecimal.ZERO) { "Cancelled Quantity [$cancelledQuantity] must be greater than or equal to 0"}

        check(totalQuantity >= executedQuantity) { "Total Quantity [$totalQuantity] must be greater than or equal to Executed Quantity [$executedQuantity]"}
        check(totalQuantity >= cancelledQuantity) { "Total Quantity [$totalQuantity] must be greater than or equal to Cancelled Quantity [$cancelledQuantity]"}

        check(totalQuantity >= executedQuantity + cancelledQuantity) { "Total Quantity [$totalQuantity] must be greater than the total of Executed [$executedQuantity] and Cancelled [$cancelledQuantity] quantities = [${executedQuantity + cancelledQuantity}]" }
    }

}