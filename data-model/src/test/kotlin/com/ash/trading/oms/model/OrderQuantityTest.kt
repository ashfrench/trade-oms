package com.ash.trading.oms.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal

class OrderQuantityTest {

    @Test
    fun `basic quantity is valid`() {
        val orderQuantity = OrderQuantity(BigDecimal.ONE)
        assertAll(
            { assertEquals(orderQuantity.totalQuantity, BigDecimal.ONE) { "Total Quantity should be equal 1" } },
            { assertEquals(orderQuantity.openQuantity, BigDecimal.ONE) { "Open Quantity should be equal 1" } },
            { assertEquals(orderQuantity.workedQuantity, BigDecimal.ZERO) { "Worked Quantity should be equal 0" } },
            { assertEquals(orderQuantity.executedQuantity, BigDecimal.ZERO) { "Executed Quantity should be equal 0" } },
            { assertEquals(orderQuantity.cancelledQuantity, BigDecimal.ZERO){ "Cancelled Quantity should be equal 0" } },
            { assertEquals(orderQuantity.usedQuantity, BigDecimal.ZERO) { "Used Quantity should be equal 0" } }
        )
    }

    @Test
    fun `worked quantity`() {
        val orderQuantity = OrderQuantity(BigDecimal.ONE, workedQuantity = BigDecimal.ONE)
        assertAll(
            { assertEquals(orderQuantity.totalQuantity, BigDecimal.ONE) { "Total Quantity should be equal 1" } },
            { assertEquals(orderQuantity.openQuantity, BigDecimal.ZERO) { "Open Quantity should be equal 0" } },
            { assertEquals(orderQuantity.workedQuantity, BigDecimal.ONE) { "Worked Quantity should be equal 1" } },
            { assertEquals(orderQuantity.executedQuantity, BigDecimal.ZERO) { "Executed Quantity should be equal 0" } },
            { assertEquals(orderQuantity.cancelledQuantity, BigDecimal.ZERO) { "Cancelled Quantity should be equal 0" } },
            { assertEquals(orderQuantity.usedQuantity, BigDecimal.ONE) { "Used Quantity should be equal 1" } }
        )
    }

    @Test
    fun `executed quantity`() {
        val orderQuantity = OrderQuantity(BigDecimal.ONE, executedQuantity = BigDecimal.ONE)
        assertAll(
            { assertEquals(orderQuantity.totalQuantity, BigDecimal.ONE) { "Total Quantity should be equal 1" } },
            { assertEquals(orderQuantity.openQuantity, BigDecimal.ZERO) { "Open Quantity should be equal 0" } },
            { assertEquals(orderQuantity.workedQuantity, BigDecimal.ZERO) { "Worked Quantity should be equal 0" } },
            { assertEquals(orderQuantity.executedQuantity, BigDecimal.ONE) { "Executed Quantity should be equal 1" } },
            { assertEquals(orderQuantity.cancelledQuantity, BigDecimal.ZERO) { "Cancelled Quantity should be equal 0" } },
            { assertEquals(orderQuantity.usedQuantity, BigDecimal.ONE) { "Used Quantity should be equal 1" } }
        )
    }

    @Test
    fun `cancelled quantity`() {
        val orderQuantity = OrderQuantity(BigDecimal.ONE, cancelledQuantity = BigDecimal.ONE)
        assertAll(
            { assertEquals(orderQuantity.totalQuantity, BigDecimal.ONE) { "Total Quantity should be equal 1" } },
            { assertEquals(orderQuantity.openQuantity, BigDecimal.ZERO) { "Open Quantity should be equal 0" } },
            { assertEquals(orderQuantity.workedQuantity, BigDecimal.ZERO) { "Worked Quantity should be equal 0" } },
            { assertEquals(orderQuantity.executedQuantity, BigDecimal.ZERO) { "Executed Quantity should be equal 0" } },
            { assertEquals(orderQuantity.cancelledQuantity, BigDecimal.ONE) { "Cancelled Quantity should be equal 1" } },
            { assertEquals(orderQuantity.usedQuantity, BigDecimal.ONE) { "Used Quantity should be equal 1" } }
        )
    }

    @Test
    fun `partial worked partial executed`() {
        val orderQuantity = OrderQuantity(BigDecimal.TWO, workedQuantity = BigDecimal.ONE, executedQuantity = BigDecimal.ONE)
        assertAll(
            { assertEquals(orderQuantity.totalQuantity, BigDecimal.TWO) { "Total Quantity should be equal 2" } },
            { assertEquals(orderQuantity.openQuantity, BigDecimal.ZERO) { "Open Quantity should be equal 0" } },
            { assertEquals(orderQuantity.workedQuantity, BigDecimal.ONE) { "Worked Quantity should be equal 1" } },
            { assertEquals(orderQuantity.executedQuantity, BigDecimal.ONE) { "Executed Quantity should be equal 1" } },
            { assertEquals(orderQuantity.cancelledQuantity, BigDecimal.ZERO) { "Cancelled Quantity should be equal 0" } },
            { assertEquals(orderQuantity.usedQuantity, BigDecimal.TWO) { "Used Quantity should be equal 2" } }
        )
    }

    @Test
    fun `partial worked partial cancelled`() {
        val orderQuantity = OrderQuantity(BigDecimal.TWO, workedQuantity = BigDecimal.ONE, cancelledQuantity = BigDecimal.ONE)
        assertAll(
            { assertEquals(orderQuantity.totalQuantity, BigDecimal.TWO) { "Total Quantity should be equal 2" } },
            { assertEquals(orderQuantity.openQuantity, BigDecimal.ZERO) { "Open Quantity should be equal 0" } },
            { assertEquals(orderQuantity.workedQuantity, BigDecimal.ONE) { "Worked Quantity should be equal 1" } },
            { assertEquals(orderQuantity.executedQuantity, BigDecimal.ZERO) { "Executed Quantity should be equal 0" } },
            { assertEquals(orderQuantity.cancelledQuantity, BigDecimal.ONE) { "Cancelled Quantity should be equal 1" } },
            { assertEquals(orderQuantity.usedQuantity, BigDecimal.TWO) { "Used Quantity should be equal 2" } }
        )
    }

    @Test
    fun `partial executed partial cancelled`() {
        val orderQuantity = OrderQuantity(BigDecimal.TWO, executedQuantity = BigDecimal.ONE, cancelledQuantity = BigDecimal.ONE)
        assertAll(
            { assertEquals(orderQuantity.totalQuantity, BigDecimal.TWO) { "Total Quantity should be equal 2" } },
            { assertEquals(orderQuantity.openQuantity, BigDecimal.ZERO) { "Open Quantity should be equal 0" } },
            { assertEquals(orderQuantity.workedQuantity, BigDecimal.ZERO) { "Worked Quantity should be equal 0" } },
            { assertEquals(orderQuantity.executedQuantity, BigDecimal.ONE) { "Executed Quantity should be equal 1" } },
            { assertEquals(orderQuantity.cancelledQuantity, BigDecimal.ONE) { "Cancelled Quantity should be equal 1" } },
            { assertEquals(orderQuantity.usedQuantity, BigDecimal.TWO) { "Used Quantity should be equal 2" } }
        )
    }

    @Test
    fun `partial worked partial executed with open quantity`() {
        val orderQuantity = OrderQuantity(BigDecimal.TEN, workedQuantity = BigDecimal.ONE, executedQuantity = BigDecimal.ONE)
        assertAll(
            { assertEquals(orderQuantity.totalQuantity, BigDecimal.TEN) { "Total Quantity should be equal 10" } },
            { assertEquals(orderQuantity.openQuantity, BigDecimal(8)) { "Open Quantity should be equal 8" } },
            { assertEquals(orderQuantity.workedQuantity, BigDecimal.ONE) { "Worked Quantity should be equal 1" } },
            { assertEquals(orderQuantity.executedQuantity, BigDecimal.ONE) { "Executed Quantity should be equal 1" } },
            { assertEquals(orderQuantity.cancelledQuantity, BigDecimal.ZERO) { "Cancelled Quantity should be equal 0" } },
            { assertEquals(orderQuantity.usedQuantity, BigDecimal.TWO) { "Used Quantity should be equal 2" } }
        )
    }

    @Test
    fun `partial worked partial cancelled with open quantity`() {
        val orderQuantity = OrderQuantity(BigDecimal.TEN, workedQuantity = BigDecimal.ONE, cancelledQuantity = BigDecimal.ONE)
        assertAll(
            { assertEquals(orderQuantity.totalQuantity, BigDecimal.TEN) { "Total Quantity should be equal 10" } },
            { assertEquals(orderQuantity.openQuantity, BigDecimal(8)) { "Open Quantity should be equal 8" } },
            { assertEquals(orderQuantity.workedQuantity, BigDecimal.ONE) { "Worked Quantity should be equal 1" } },
            { assertEquals(orderQuantity.executedQuantity, BigDecimal.ZERO) { "Executed Quantity should be equal 0" } },
            { assertEquals(orderQuantity.cancelledQuantity, BigDecimal.ONE) { "Cancelled Quantity should be equal 1" } },
            { assertEquals(orderQuantity.usedQuantity, BigDecimal.TWO) { "Used Quantity should be equal 2" } }
        )
    }

    @Test
    fun `partial executed partial cancelled with open quantity`() {
        val orderQuantity = OrderQuantity(BigDecimal.TEN, executedQuantity = BigDecimal.ONE, cancelledQuantity = BigDecimal.ONE)
        assertAll(
            { assertEquals(orderQuantity.totalQuantity, BigDecimal.TEN) { "Total Quantity should be equal 10" } },
            { assertEquals(orderQuantity.openQuantity, BigDecimal(8)) { "Open Quantity should be equal 8" } },
            { assertEquals(orderQuantity.workedQuantity, BigDecimal.ZERO) { "Worked Quantity should be equal 0" } },
            { assertEquals(orderQuantity.executedQuantity, BigDecimal.ONE) { "Executed Quantity should be equal 1" } },
            { assertEquals(orderQuantity.cancelledQuantity, BigDecimal.ONE) { "Cancelled Quantity should be equal 1" } },
            { assertEquals(orderQuantity.usedQuantity, BigDecimal.TWO) { "Used Quantity should be equal 2" } }
        )
    }

    @Test
    fun `invalid positive worked quantity`() {
        val exception = assertThrows<IllegalStateException>("Worked Quantity Is Bigger Than Total Quantity") { OrderQuantity(BigDecimal.ONE, workedQuantity = BigDecimal.TWO) }
        assertEquals("Total Quantity [1] must be greater than or equal to Worked Quantity [2]", exception.message)
    }

    @Test
    fun `invalid negative worked quantity`() {
        val exception = assertThrows<IllegalStateException>("Worked Quantity Is Less Than Zero") { OrderQuantity(BigDecimal.ONE, workedQuantity = BigDecimal(-1)) }
        assertEquals("Worked Quantity [-1] must be greater than or equal to 0", exception.message)
    }

    @Test
    fun `invalid positive executed quantity`() {
        val exception = assertThrows<IllegalStateException>("Executed Quantity Is Bigger Than Total Quantity") { OrderQuantity(BigDecimal.ONE, executedQuantity = BigDecimal.TWO) }
        assertEquals("Total Quantity [1] must be greater than or equal to Executed Quantity [2]", exception.message)
    }

    @Test
    fun `invalid negative executed quantity`() {
        val exception = assertThrows<IllegalStateException>("Executed Quantity Is Less Than Zero") { OrderQuantity(BigDecimal.ONE, executedQuantity = BigDecimal(-1)) }
        assertEquals("Executed Quantity [-1] must be greater than or equal to 0", exception.message)
    }

    @Test
    fun `invalid positive cancelled quantity`() {
        val exception = assertThrows<IllegalStateException>("Cancelled Quantity Is Bigger Than Total Quantity") { OrderQuantity(BigDecimal.ONE, cancelledQuantity = BigDecimal.TWO) }
        assertEquals("Total Quantity [1] must be greater than or equal to Cancelled Quantity [2]", exception.message)
    }

    @Test
    fun `invalid negative cancelled quantity`() {
        val exception = assertThrows<IllegalStateException>("Cancelled Quantity Is Less Than Zero") { OrderQuantity(BigDecimal.ONE, cancelledQuantity = BigDecimal(-1)) }
        assertEquals("Cancelled Quantity [-1] must be greater than or equal to 0", exception.message)
    }

    @Test
    fun `used quantity exceeds total quantity`() {
        val exception = assertThrows<IllegalStateException>("Used Quantity Is Greater Than Total") { OrderQuantity(BigDecimal.TWO, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE) }
        assertEquals("Total Quantity [2] must be greater than the total of Executed [1], Worked Quantity [1] and Cancelled [1] quantities = [3]", exception.message)
    }

}