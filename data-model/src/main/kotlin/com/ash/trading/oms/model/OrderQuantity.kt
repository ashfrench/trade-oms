package com.ash.trading.oms.model

import java.math.BigDecimal

data class OrderQuantity(
    val totalQuantity: BigDecimal,
    val openQuantity: BigDecimal,
    val executedQuantity: BigDecimal,
    val acknowledgedQuantity: BigDecimal
) {
    constructor(totalQuantity: BigDecimal): this(totalQuantity, totalQuantity, BigDecimal.ZERO, BigDecimal.ZERO)

    init {
        validate()
    }

    private fun validate() {
        check(totalQuantity >= openQuantity + executedQuantity + acknowledgedQuantity) { "Total Quantity [$totalQuantity] must be more than the total of Executed [$executedQuantity], Open [$openQuantity] and Acknowledged [$acknowledgedQuantity] quantities = [${openQuantity + executedQuantity + acknowledgedQuantity}]" }
    }
}