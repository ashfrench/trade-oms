package com.ash.trading.oms.model

import java.math.BigDecimal

data class TradeQuantity(
    val totalQuantity: TotalQuantity,
    val workedQuantities: Map<EMSOrderId, WorkedQuantity> = emptyMap(),
    val executedQuantities: Map<EMSOrderId, ExecutedQuantity> = emptyMap(),
    val confirmedQuantities: Map<EMSOrderId, ConfirmedQuantity> = emptyMap(),
    val cancelledQuantity: CancelledQuantity = CancelledQuantity(BigDecimal.ZERO)
) {
    val workedQuantity: WorkedQuantity = workedQuantities.values.sumOf { it }
    val executedQuantity: ExecutedQuantity = executedQuantities.values.sumOf { it }
    val confirmedQuantity: ConfirmedQuantity = confirmedQuantities.values.sumOf { it }
    val usedQuantity: UsedQuantity = executedQuantity + workedQuantity + cancelledQuantity + confirmedQuantity
    val openQuantity: OpenQuantity = totalQuantity - usedQuantity

    init {
        validate()
    }

    private fun validate() {
        check(totalQuantity > BigDecimal.ZERO) { "Total Quantity [$totalQuantity] must be greater than 0"}
        check(workedQuantity >= BigDecimal.ZERO) { "Worked Quantity [$workedQuantity] must be greater than or equal to 0"}
        check(executedQuantity >= BigDecimal.ZERO) { "Executed Quantity [$executedQuantity] must be greater than or equal to 0"}
        check(cancelledQuantity >= BigDecimal.ZERO) { "Cancelled Quantity [${cancelledQuantity.quantity}] must be greater than or equal to 0"}
        check(confirmedQuantity >= BigDecimal.ZERO) { "Confirmed Quantity [$confirmedQuantity] must be greater than or equal to 0"}

        check(totalQuantity >= executedQuantity) { "Total Quantity [$totalQuantity] must be greater than or equal to Executed Quantity [$executedQuantity]"}
        check(totalQuantity >= workedQuantity) { "Total Quantity [$totalQuantity] must be greater than or equal to Worked Quantity [$workedQuantity]"}
        check(totalQuantity >= cancelledQuantity) { "Total Quantity [$totalQuantity] must be greater than or equal to Cancelled Quantity [${cancelledQuantity.quantity}]"}
        check(totalQuantity >= confirmedQuantity) { "Total Quantity [$totalQuantity] must be greater than or equal to Confirmed Quantity [$confirmedQuantity]"}

        check(workedQuantities.all { it.value >= BigDecimal.ZERO }) { "All worked quantities must be positive" }
        check(executedQuantities.all { it.value >= BigDecimal.ZERO }) { "All executed quantities must be positive" }
        check(confirmedQuantities.all { it.value >= BigDecimal.ZERO }) { "All confirm quantities must be positive" }

        check(totalQuantity >= executedQuantity + workedQuantity + cancelledQuantity) { "Total Quantity [$totalQuantity] must be greater than the total of Executed [$executedQuantity], Worked Quantity [$workedQuantity] and Cancelled [${cancelledQuantity.quantity}] quantities = [${executedQuantity + workedQuantity + cancelledQuantity}]" }
    }
}



