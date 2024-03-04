package com.ash.trading.oms.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal

class TradeOrderQuantitiesTest {

    @Test
    fun `basic quantity is valid`() {
        val orderQuantity = TradeOrderQuantities(mapOf(newOrderId() to BigDecimal.ONE))
        assertAll(
            { assertEquals(orderQuantity.totalQuantity, BigDecimal.ONE) { "Total Quantity should be equal 1" } },
            { assertEquals(orderQuantity.openQuantity, BigDecimal.ONE) { "Open Quantity should be equal 1" } },
            { assertEquals(orderQuantity.executedQuantity, BigDecimal.ZERO) { "Executed Quantity should be equal 0" } },
            { assertEquals(orderQuantity.cancelledQuantity, BigDecimal.ZERO){ "Cancelled Quantity should be equal 0" } },
            { assertEquals(orderQuantity.usedQuantity, BigDecimal.ZERO) { "Used Quantity should be equal 0" } }
        )
    }

    @Test
    fun `invalid order quantity`() {
        val exception = assertThrows<IllegalStateException>("All Order Quantities must be greater than 0") {
            TradeOrderQuantities(mapOf(newOrderId() to BigDecimal(-1)))
        }
        assertEquals("All Order Quantities must be greater than 0", exception.message)
    }

    @Test
    fun `invalid trade quantity`() {
        val exception = assertThrows<IllegalStateException>("All Trade Quantities must be greater than 0") {
            TradeOrderQuantities(mapOf(newOrderId() to BigDecimal.ONE), mapOf(newTradeId() to BigDecimal(-1)))
        }
        assertEquals("All Trade Quantities must be greater than 0", exception.message)
    }

}