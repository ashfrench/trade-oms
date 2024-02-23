package com.ash.trading.oms.model

import java.math.BigDecimal

data class OrderQuantity(
    val totalQuantity: TotalQuantity,
    val openQuantity: OpenQuantity,
    val executedQuantity: ExecutedQuantity,
    val workedQuantity: WorkedQuantity,
    val cancelledQuantity: CancelledQuantity
) {
    constructor(totalQuantity: TotalQuantity): this(totalQuantity, totalQuantity, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO)

    init {
        validate()
    }

    private fun validate() {
        check(totalQuantity >= openQuantity + executedQuantity + workedQuantity + cancelledQuantity) { "Total Quantity [$totalQuantity] must be more than the total of Executed [$executedQuantity], Open [$openQuantity] and Acknowledged [$workedQuantity] and Cancelled [$cancelledQuantity] quantities = [${openQuantity + executedQuantity + workedQuantity + cancelledQuantity}]" }
    }
}

typealias TotalQuantity = BigDecimal
typealias OpenQuantity = BigDecimal
typealias ExecutedQuantity = BigDecimal
typealias WorkedQuantity = BigDecimal
typealias CancelledQuantity = BigDecimal