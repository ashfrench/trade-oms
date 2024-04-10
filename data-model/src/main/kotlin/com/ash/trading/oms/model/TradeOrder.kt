package com.ash.trading.oms.model

import java.math.BigDecimal
import java.time.LocalDate

data class TradeOrder(
    val tradeOrderId: TradeOrderId = newTradeOrderId(),
    val traderId: TraderId,
    val orderQuantities: Map<OrderId, WorkedQuantity>,
    val tradeQuantities: Map<TradeId, TradeQuantity> = emptyMap(),
    val tradeOrderState: TradeOrderState = TradeOrderState.NEW,
    val reportedDate: LocalDate? = null
) {

    val totalQuantity: TotalQuantity = orderQuantities.values.sumOf { it }
    val executedQuantity: ExecutedQuantity = tradeQuantities.values.sumOf { it }
    val openQuantity: OpenQuantity = totalQuantity - executedQuantity

    init {
        validate()
    }

    private fun validate() {
        validateState()
        check(totalQuantity > BigDecimal.ZERO) { "Total Quantity : $totalQuantity should be greater than 0" }
    }

    private fun validateState() {
        when (tradeOrderState) {
            TradeOrderState.NEW -> check(executedQuantity == BigDecimal.ZERO)
            TradeOrderState.PARTIALLY_EXECUTED -> check(openQuantity > BigDecimal.ZERO)
            TradeOrderState.EXECUTED -> check(openQuantity == BigDecimal.ZERO)
            TradeOrderState.REPORTED -> check(openQuantity == BigDecimal.ZERO && reportedDate != null)
        }
    }
}

