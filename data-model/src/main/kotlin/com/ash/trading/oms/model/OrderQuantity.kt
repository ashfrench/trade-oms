package com.ash.trading.oms.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class OrderQuantity(
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
        check(cancelledQuantity >= BigDecimal.ZERO) { "Cancelled Quantity [$cancelledQuantity] must be greater than or equal to 0"}

        check(totalQuantity >= executedQuantity) { "Total Quantity [$totalQuantity] must be greater than or equal to Executed Quantity [$executedQuantity]"}
        check(totalQuantity >= workedQuantity) { "Total Quantity [$totalQuantity] must be greater than or equal to Worked Quantity [$workedQuantity]"}
        check(totalQuantity >= cancelledQuantity) { "Total Quantity [$totalQuantity] must be greater than or equal to Cancelled Quantity [$cancelledQuantity]"}

        check(totalQuantity >= executedQuantity + workedQuantity + cancelledQuantity) { "Total Quantity [$totalQuantity] must be greater than the total of Executed [$executedQuantity], Worked Quantity [$workedQuantity] and Cancelled [$cancelledQuantity] quantities = [${executedQuantity + workedQuantity + cancelledQuantity}]" }
    }
}

typealias TotalQuantity = BigDecimal
typealias OpenQuantity = BigDecimal
typealias ExecutedQuantity = BigDecimal
typealias WorkedQuantity = BigDecimal
typealias TradeQuantity = BigDecimal
typealias UsedQuantity = BigDecimal
typealias Quantity = BigDecimal

operator fun CancelledQuantity.compareTo(bigDecimal: BigDecimal) = quantity.compareTo(bigDecimal)
operator fun BigDecimal.plus(cancelledQuantity: CancelledQuantity) = plus(cancelledQuantity.quantity)
operator fun BigDecimal.compareTo(cancelledQuantity: CancelledQuantity) = compareTo(cancelledQuantity.quantity)
data class CancelledQuantity(val quantity: BigDecimal, val cancelledTime: LocalDateTime? = null) {
    init {
        validate()
    }

    private fun validate() {
        check(quantity > BigDecimal.ZERO && cancelledTime != null ) { "Cancelled Time Must not be null when cancelled quantity is set"}
    }


}
