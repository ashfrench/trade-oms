package com.ash.trading.oms.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class CancelledQuantity(val quantity: BigDecimal, val cancelledTime: LocalDateTime? = null) {
    init {
        validate()
    }

    constructor(quantity: BigDecimal): this(quantity, if(quantity > BigDecimal.ZERO) LocalDateTime.now() else null)

    private fun validate() {
        check(quantity >= BigDecimal.ZERO) { "Cancelled Quantity [${quantity}] must be greater than or equal to 0"}
        check((quantity > BigDecimal.ZERO && cancelledTime != null) || (quantity == BigDecimal.ZERO && cancelledTime == null) ) { "Cancelled Time Must not be null when cancelled quantity is set"}
    }

}