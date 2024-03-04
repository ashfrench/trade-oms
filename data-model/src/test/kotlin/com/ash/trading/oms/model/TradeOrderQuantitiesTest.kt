package com.ash.trading.oms.model

import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal

class TradeOrderQuantitiesTest {

    @Test
    fun `basic quantity is valid`() {
        val orderQuantity = TradeOrderQuantities(mapOf(newOrderId() to BigDecimal.ONE))
        assertAll(
            { assertEquals(BigDecimal.ONE, orderQuantity.totalQuantity) { "Total Quantity should be equal 1" } },
            { assertEquals(BigDecimal.ONE, orderQuantity.openQuantity) { "Open Quantity should be equal 1" } },
            { assertEquals(BigDecimal.ZERO, orderQuantity.executedQuantity) { "Executed Quantity should be equal 0" } },
            { assertEquals(BigDecimal.ZERO, orderQuantity.cancelledQuantity) { "Cancelled Quantity should be equal 0" } },
            { assertEquals(BigDecimal.ZERO, orderQuantity.usedQuantity) { "Used Quantity should be equal 0" } }
        )
    }

    @Test
    fun `basic multi order quantity is valid`() {
        val orderQuantity = TradeOrderQuantities(mapOf(newOrderId() to BigDecimal.ONE, newOrderId() to BigDecimal.ONE))
        assertAll(
            { assertEquals(BigDecimal.TWO, orderQuantity.totalQuantity) { "Total Quantity should be equal 2" } },
            { assertEquals(BigDecimal.TWO, orderQuantity.openQuantity) { "Open Quantity should be equal 2" } },
            { assertEquals(BigDecimal.ZERO, orderQuantity.executedQuantity) { "Executed Quantity should be equal 0" } },
            { assertEquals(BigDecimal.ZERO, orderQuantity.cancelledQuantity) { "Cancelled Quantity should be equal 0" } },
            { assertEquals(BigDecimal.ZERO, orderQuantity.usedQuantity) { "Used Quantity should be equal 0" } }
        )
    }

    @Test
    fun `basic multi order quantity with trade is valid`() {
        val orderQuantity = TradeOrderQuantities(
            mapOf(newOrderId() to BigDecimal.ONE, newOrderId() to BigDecimal.ONE),
            mapOf(newTradeId() to BigDecimal.TWO)
        )
        assertAll(
            { assertEquals(BigDecimal.TWO, orderQuantity.totalQuantity) { "Total Quantity should be equal 2" } },
            { assertEquals(BigDecimal.ZERO, orderQuantity.openQuantity) { "Open Quantity should be equal 0" } },
            { assertEquals(BigDecimal.TWO, orderQuantity.executedQuantity) { "Executed Quantity should be equal 2" } },
            { assertEquals(BigDecimal.ZERO, orderQuantity.cancelledQuantity) { "Cancelled Quantity should be equal 0" } },
            { assertEquals(BigDecimal.TWO, orderQuantity.usedQuantity) { "Used Quantity should be equal 2" } }
        )
    }

    @Test
    fun `basic multi order quantity with multi trade is valid`() {
        val orderQuantity = TradeOrderQuantities(
            mapOf(newOrderId() to BigDecimal.ONE, newOrderId() to BigDecimal.ONE),
            mapOf(newTradeId() to BigDecimal.ONE, newTradeId() to BigDecimal.ONE)
        )
        assertAll(
            { assertEquals(BigDecimal.TWO, orderQuantity.totalQuantity) { "Total Quantity should be equal 2" } },
            { assertEquals(BigDecimal.ZERO, orderQuantity.openQuantity) { "Open Quantity should be equal 0" } },
            { assertEquals(BigDecimal.TWO, orderQuantity.executedQuantity) { "Executed Quantity should be equal 2" } },
            { assertEquals(BigDecimal.ZERO, orderQuantity.cancelledQuantity) { "Cancelled Quantity should be equal 0" } },
            { assertEquals(BigDecimal.TWO, orderQuantity.usedQuantity) { "Used Quantity should be equal 2" } }
        )
    }

    @Test
    fun `basic single order quantity with multi trade is valid`() {
        val orderQuantity = TradeOrderQuantities(
            mapOf(newOrderId() to BigDecimal.TWO),
            mapOf(newTradeId() to BigDecimal.ONE, newTradeId() to BigDecimal.ONE)
        )
        assertAll(
            { assertEquals(BigDecimal.TWO, orderQuantity.totalQuantity) { "Total Quantity should be equal 2" } },
            { assertEquals(BigDecimal.ZERO, orderQuantity.openQuantity) { "Open Quantity should be equal 0" } },
            { assertEquals(BigDecimal.TWO, orderQuantity.executedQuantity) { "Executed Quantity should be equal 2" } },
            { assertEquals(BigDecimal.ZERO, orderQuantity.cancelledQuantity) { "Cancelled Quantity should be equal 0" } },
            { assertEquals(BigDecimal.TWO, orderQuantity.usedQuantity) { "Used Quantity should be equal 2" } }
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

    @Test
    fun `trade too large quantity`() {
        val exception = assertThrows<IllegalStateException>("Total trade quantity must be less than our equal to total order quantity") {
            TradeOrderQuantities(mapOf(newOrderId() to BigDecimal.ONE), mapOf(newTradeId() to BigDecimal.TWO))
        }
        assertEquals("Total Quantity [1] must be greater than or equal to Executed Quantity [2]", exception.message)
    }
}