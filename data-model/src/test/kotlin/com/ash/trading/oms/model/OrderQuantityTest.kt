package com.ash.trading.oms.model

import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.time.LocalDateTime

class OrderQuantityTest {

    @Test
    fun `basic quantity is valid`() {
        val orderQuantity = OrderQuantity(BigDecimal.ONE)
        assertAll(
            { assertEquals(BigDecimal.ONE, orderQuantity.totalQuantity) { "Total Quantity should be equal 1" } },
            { assertEquals(BigDecimal.ONE, orderQuantity.openQuantity) { "Open Quantity should be equal 1" } },
            { assertEquals(BigDecimal.ZERO, orderQuantity.workedQuantity) { "Worked Quantity should be equal 0" } },
            { assertEquals(BigDecimal.ZERO, orderQuantity.executedQuantity) { "Executed Quantity should be equal 0" } },
            { assertEquals(CancelledQuantity(BigDecimal.ZERO), orderQuantity.cancelledQuantity) { "Cancelled Quantity should be equal 0" } },
            { assertEquals(BigDecimal.ZERO, orderQuantity.usedQuantity) { "Used Quantity should be equal 0" } }
        )
    }

    @Test
    fun `worked quantity`() {
        val orderQuantity = OrderQuantity(BigDecimal.ONE, workedQuantity = BigDecimal.ONE)
        assertAll(
            { assertEquals(BigDecimal.ONE, orderQuantity.totalQuantity) { "Total Quantity should be equal 1" } },
            { assertEquals(BigDecimal.ZERO, orderQuantity.openQuantity) { "Open Quantity should be equal 0" } },
            { assertEquals(BigDecimal.ONE, orderQuantity.workedQuantity) { "Worked Quantity should be equal 1" } },
            { assertEquals(BigDecimal.ZERO, orderQuantity.executedQuantity) { "Executed Quantity should be equal 0" } },
            { assertEquals(CancelledQuantity(BigDecimal.ZERO), orderQuantity.cancelledQuantity) { "Cancelled Quantity should be equal 0" } },
            { assertEquals(BigDecimal.ONE, orderQuantity.usedQuantity) { "Used Quantity should be equal 1" } }
        )
    }

    @Test
    fun `executed quantity`() {
        val orderQuantity = OrderQuantity(BigDecimal.ONE, executedQuantity = BigDecimal.ONE)
        assertAll(
            { assertEquals(BigDecimal.ONE, orderQuantity.totalQuantity) { "Total Quantity should be equal 1" } },
            { assertEquals(BigDecimal.ZERO, orderQuantity.openQuantity) { "Open Quantity should be equal 0" } },
            { assertEquals(BigDecimal.ZERO, orderQuantity.workedQuantity) { "Worked Quantity should be equal 0" } },
            { assertEquals(BigDecimal.ONE, orderQuantity.executedQuantity) { "Executed Quantity should be equal 1" } },
            { assertEquals(CancelledQuantity(BigDecimal.ZERO), orderQuantity.cancelledQuantity) { "Cancelled Quantity should be equal 0" } },
            { assertEquals(BigDecimal.ONE, orderQuantity.usedQuantity) { "Used Quantity should be equal 1" } }
        )
    }

    @Test
    fun `cancelled quantity`() {
        val cancelledTime = LocalDateTime.now()
        val orderQuantity = OrderQuantity(BigDecimal.ONE, cancelledQuantity = CancelledQuantity(BigDecimal.ONE,
            cancelledTime
        ))
        assertAll(
            { assertEquals(BigDecimal.ONE, orderQuantity.totalQuantity) { "Total Quantity should be equal 1" } },
            { assertEquals(BigDecimal.ZERO, orderQuantity.openQuantity) { "Open Quantity should be equal 0" } },
            { assertEquals(BigDecimal.ZERO, orderQuantity.workedQuantity) { "Worked Quantity should be equal 0" } },
            { assertEquals(BigDecimal.ZERO, orderQuantity.executedQuantity) { "Executed Quantity should be equal 0" } },
            { assertEquals(CancelledQuantity(BigDecimal.ONE, cancelledTime), orderQuantity.cancelledQuantity) { "Cancelled Quantity should be equal 1" } },
            { assertEquals(BigDecimal.ONE, orderQuantity.usedQuantity) { "Used Quantity should be equal 1" } }
        )
    }

    @Test
    fun `partial worked partial executed`() {
        val orderQuantity = OrderQuantity(BigDecimal.TWO, workedQuantity = BigDecimal.ONE, executedQuantity = BigDecimal.ONE)
        assertAll(
            { assertEquals(BigDecimal.TWO, orderQuantity.totalQuantity) { "Total Quantity should be equal 2" } },
            { assertEquals(BigDecimal.ZERO, orderQuantity.openQuantity) { "Open Quantity should be equal 0" } },
            { assertEquals(BigDecimal.ONE, orderQuantity.workedQuantity) { "Worked Quantity should be equal 1" } },
            { assertEquals(BigDecimal.ONE, orderQuantity.executedQuantity) { "Executed Quantity should be equal 1" } },
            { assertEquals(CancelledQuantity(BigDecimal.ZERO), orderQuantity.cancelledQuantity) { "Cancelled Quantity should be equal 0" } },
            { assertEquals(BigDecimal.TWO, orderQuantity.usedQuantity) { "Used Quantity should be equal 2" } }
        )
    }

    @Test
    fun `partial worked partial cancelled`() {
        val cancelledTime = LocalDateTime.now()
        val orderQuantity = OrderQuantity(BigDecimal.TWO, workedQuantity = BigDecimal.ONE, cancelledQuantity = CancelledQuantity(BigDecimal.ONE,
            cancelledTime
        ))
        assertAll(
            { assertEquals(BigDecimal.TWO, orderQuantity.totalQuantity) { "Total Quantity should be equal 2" } },
            { assertEquals(BigDecimal.ZERO, orderQuantity.openQuantity) { "Open Quantity should be equal 0" } },
            { assertEquals(BigDecimal.ONE, orderQuantity.workedQuantity) { "Worked Quantity should be equal 1" } },
            { assertEquals(BigDecimal.ZERO, orderQuantity.executedQuantity) { "Executed Quantity should be equal 0" } },
            { assertEquals(CancelledQuantity(BigDecimal.ONE, cancelledTime), orderQuantity.cancelledQuantity) { "Cancelled Quantity should be equal 1" } },
            { assertEquals(BigDecimal.TWO, orderQuantity.usedQuantity) { "Used Quantity should be equal 2" } }
        )
    }

    @Test
    fun `partial executed partial cancelled`() {
        val cancelledTime = LocalDateTime.now()
        val orderQuantity = OrderQuantity(BigDecimal.TWO, executedQuantity = BigDecimal.ONE, cancelledQuantity = CancelledQuantity(BigDecimal.ONE,
            cancelledTime
        ))
        assertAll(
            { assertEquals(BigDecimal.TWO, orderQuantity.totalQuantity) { "Total Quantity should be equal 2" } },
            { assertEquals(BigDecimal.ZERO, orderQuantity.openQuantity) { "Open Quantity should be equal 0" } },
            { assertEquals(BigDecimal.ZERO, orderQuantity.workedQuantity) { "Worked Quantity should be equal 0" } },
            { assertEquals(BigDecimal.ONE, orderQuantity.executedQuantity) { "Executed Quantity should be equal 1" } },
            { assertEquals(CancelledQuantity(BigDecimal.ONE, cancelledTime), orderQuantity.cancelledQuantity) { "Cancelled Quantity should be equal 1" } },
            { assertEquals(BigDecimal.TWO, orderQuantity.usedQuantity) { "Used Quantity should be equal 2" } }
        )
    }

    @Test
    fun `partial worked partial executed with open quantity`() {
        val orderQuantity = OrderQuantity(BigDecimal.TEN, workedQuantity = BigDecimal.ONE, executedQuantity = BigDecimal.ONE)
        assertAll(
            { assertEquals(BigDecimal.TEN, orderQuantity.totalQuantity) { "Total Quantity should be equal 10" } },
            { assertEquals(BigDecimal(8), orderQuantity.openQuantity) { "Open Quantity should be equal 8" } },
            { assertEquals(BigDecimal.ONE, orderQuantity.workedQuantity) { "Worked Quantity should be equal 1" } },
            { assertEquals(BigDecimal.ONE, orderQuantity.executedQuantity) { "Executed Quantity should be equal 1" } },
            { assertEquals(CancelledQuantity(BigDecimal.ZERO), orderQuantity.cancelledQuantity) { "Cancelled Quantity should be equal 0" } },
            { assertEquals(BigDecimal.TWO, orderQuantity.usedQuantity) { "Used Quantity should be equal 2" } }
        )
    }

    @Test
    fun `partial worked partial cancelled with open quantity`() {
        val cancelledTime = LocalDateTime.now()
        val orderQuantity = OrderQuantity(BigDecimal.TEN, workedQuantity = BigDecimal.ONE, cancelledQuantity = CancelledQuantity(BigDecimal.ONE,
            cancelledTime
        ))
        assertAll(
            { assertEquals(BigDecimal.TEN, orderQuantity.totalQuantity) { "Total Quantity should be equal 10" } },
            { assertEquals(BigDecimal(8), orderQuantity.openQuantity) { "Open Quantity should be equal 8" } },
            { assertEquals(BigDecimal.ONE, orderQuantity.workedQuantity) { "Worked Quantity should be equal 1" } },
            { assertEquals(BigDecimal.ZERO, orderQuantity.executedQuantity) { "Executed Quantity should be equal 0" } },
            { assertEquals(CancelledQuantity(BigDecimal.ONE, cancelledTime), orderQuantity.cancelledQuantity) { "Cancelled Quantity should be equal 1" } },
            { assertEquals(BigDecimal.TWO, orderQuantity.usedQuantity) { "Used Quantity should be equal 2" } }
        )
    }

    @Test
    fun `partial executed partial cancelled with open quantity`() {
        val cancelledTime = LocalDateTime.now()
        val orderQuantity = OrderQuantity(BigDecimal.TEN, executedQuantity = BigDecimal.ONE, cancelledQuantity = CancelledQuantity(BigDecimal.ONE,
            cancelledTime
        ))
        assertAll(
            { assertEquals(BigDecimal.TEN, orderQuantity.totalQuantity) { "Total Quantity should be equal 10" } },
            { assertEquals(BigDecimal(8), orderQuantity.openQuantity) { "Open Quantity should be equal 8" } },
            { assertEquals(BigDecimal.ZERO, orderQuantity.workedQuantity) { "Worked Quantity should be equal 0" } },
            { assertEquals(BigDecimal.ONE, orderQuantity.executedQuantity) { "Executed Quantity should be equal 1" } },
            { assertEquals(CancelledQuantity(BigDecimal.ONE, cancelledTime), orderQuantity.cancelledQuantity) { "Cancelled Quantity should be equal 1" } },
            { assertEquals(BigDecimal.TWO, orderQuantity.usedQuantity) { "Used Quantity should be equal 2" } }
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
        val exception = assertThrows<IllegalStateException>("Cancelled Quantity Is Bigger Than Total Quantity") { OrderQuantity(BigDecimal.ONE, cancelledQuantity = CancelledQuantity(BigDecimal.TWO, LocalDateTime.now())) }
        assertEquals("Total Quantity [1] must be greater than or equal to Cancelled Quantity [2]", exception.message)
    }

    @Test
    fun `invalid negative cancelled quantity`() {
        val exception = assertThrows<IllegalStateException>("Cancelled Quantity Is Less Than Zero") { OrderQuantity(BigDecimal.ONE, cancelledQuantity = CancelledQuantity(BigDecimal(-1), LocalDateTime.now())) }
        assertEquals("Cancelled Quantity [-1] must be greater than or equal to 0", exception.message)
    }

    @Test
    fun `used quantity exceeds total quantity`() {
        val exception = assertThrows<IllegalStateException>("Used Quantity Is Greater Than Total") { OrderQuantity(BigDecimal.TWO, BigDecimal.ONE, BigDecimal.ONE, CancelledQuantity(BigDecimal.ONE, LocalDateTime.now())) }
        assertEquals("Total Quantity [2] must be greater than the total of Executed [1], Worked Quantity [1] and Cancelled [1] quantities = [3]", exception.message)
    }

}