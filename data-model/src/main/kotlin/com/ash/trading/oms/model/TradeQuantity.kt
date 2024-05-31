package com.ash.trading.oms.model

import java.math.BigDecimal

data class TradeQuantity(
    val totalQuantity: TotalQuantity,
    val workedQuantity: WorkedQuantity = BigDecimal.ZERO,
    val executedQuantity: ExecutedQuantity = BigDecimal.ZERO,
    val cancelledQuantity: CancelledQuantity = CancelledQuantity(BigDecimal.ZERO)
) {
    val usedQuantity: UsedQuantity = executedQuantity + workedQuantity + cancelledQuantity
    val openQuantity: OpenQuantity = totalQuantity - usedQuantity

    init {
        validate()
    }

    private fun validate() {
        check(totalQuantity > BigDecimal.ZERO) { "Total Quantity [$totalQuantity] must be greater than 0"}
        check(workedQuantity >= BigDecimal.ZERO) { "Worked Quantity [$workedQuantity] must be greater than or equal to 0"}
        check(executedQuantity >= BigDecimal.ZERO) { "Executed Quantity [$executedQuantity] must be greater than or equal to 0"}
        check(cancelledQuantity >= BigDecimal.ZERO) { "Cancelled Quantity [${cancelledQuantity.quantity}] must be greater than or equal to 0"}

        check(totalQuantity >= executedQuantity) { "Total Quantity [$totalQuantity] must be greater than or equal to Executed Quantity [$executedQuantity]"}
        check(totalQuantity >= workedQuantity) { "Total Quantity [$totalQuantity] must be greater than or equal to Worked Quantity [$workedQuantity]"}
        check(totalQuantity >= cancelledQuantity) { "Total Quantity [$totalQuantity] must be greater than or equal to Cancelled Quantity [${cancelledQuantity.quantity}]"}

        check(totalQuantity >= executedQuantity + workedQuantity + cancelledQuantity) { "Total Quantity [$totalQuantity] must be greater than the total of Executed [$executedQuantity], Worked Quantity [$workedQuantity] and Cancelled [${cancelledQuantity.quantity}] quantities = [${executedQuantity + workedQuantity + cancelledQuantity}]" }
    }
}



