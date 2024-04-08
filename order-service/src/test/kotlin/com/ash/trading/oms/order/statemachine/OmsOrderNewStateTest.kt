package com.ash.trading.oms.order.statemachine

import com.ash.trading.oms.model.CancelledQuantity
import com.ash.trading.oms.model.OrderQuantity
import com.ash.trading.oms.model.newTradeId
import com.ash.trading.oms.order.statemachine.events.OrderCancelledEvent
import com.ash.trading.oms.order.statemachine.events.TraderExecutedEvent
import com.ash.trading.oms.order.statemachine.events.TraderWorkingEvent
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.time.LocalDateTime

class OmsOrderNewStateTest {

    @Test
    fun `can transition from new to worked`() {
        val orderQuantity = OrderQuantity(BigDecimal.TEN)
        val (updatedOrderQuantity, updatedState) = OmsOrderState.NEW.handleEvent(
            orderQuantity,
            TraderWorkingEvent(newTradeId(), BigDecimal.TEN)
        )

        assertAll(
            { assertEquals(BigDecimal.TEN, updatedOrderQuantity.totalQuantity) { "Total Quantity should be equal 10" } },
            { assertEquals(BigDecimal.ZERO, updatedOrderQuantity.openQuantity) { "Open Quantity should be equal 0" } },
            { assertEquals(BigDecimal.TEN, updatedOrderQuantity.workedQuantity) { "Worked Quantity should be equal 0" } },
            { assertEquals(BigDecimal.ZERO, updatedOrderQuantity.executedQuantity) { "Executed Quantity should be equal 10" } },
            { assertEquals(CancelledQuantity(BigDecimal.ZERO), updatedOrderQuantity.cancelledQuantity) { "Cancelled Quantity should be equal 0" } },
            { assertEquals(BigDecimal.TEN, updatedOrderQuantity.usedQuantity) { "Used Quantity should be equal 10" } },
            { assertEquals(OmsOrderState.WORKED, updatedState) }
        )
    }

    @Test
    fun `can transition from new to cancelled`() {
        val orderQuantity = OrderQuantity(BigDecimal.TEN)
        val cancelledTime = LocalDateTime.now()
        val (updatedOrderQuantity, updatedState) = OmsOrderState.NEW.handleEvent(
            orderQuantity,
            OrderCancelledEvent(cancelledTime)
        )

        assertAll(
            { assertEquals(BigDecimal.TEN, updatedOrderQuantity.totalQuantity) { "Total Quantity should be equal 10" } },
            { assertEquals(BigDecimal.ZERO, updatedOrderQuantity.openQuantity) { "Open Quantity should be equal 0" } },
            { assertEquals(BigDecimal.ZERO, updatedOrderQuantity.workedQuantity) { "Worked Quantity should be equal 0" } },
            { assertEquals(BigDecimal.ZERO, updatedOrderQuantity.executedQuantity) { "Executed Quantity should be equal 0" } },
            { assertEquals(CancelledQuantity(BigDecimal.TEN, cancelledTime), updatedOrderQuantity.cancelledQuantity) { "Cancelled Quantity should be equal 10" } },
            { assertEquals(BigDecimal.TEN, updatedOrderQuantity.usedQuantity) { "Used Quantity should be equal 10" } },
            { assertEquals(OmsOrderState.CANCELLED, updatedState) }
        )
    }

    @Test
    fun `can handle no transition from new for executed event`() {
        val orderQuantity = OrderQuantity(BigDecimal.TEN)
        val (updatedOrderQuantity, updatedState) = OmsOrderState.NEW.handleEvent(
            orderQuantity,
            TraderExecutedEvent(newTradeId(), BigDecimal.TEN)
        )

        assertAll(
            { assertEquals(updatedOrderQuantity, orderQuantity) },
            { assertEquals(OmsOrderState.NEW, updatedState) }
        )
    }

    @Test
    fun `can handle invalid worked amount from new`() {
        val orderQuantity = OrderQuantity(BigDecimal.TWO)
        val (updatedOrderQuantity, updatedState) = OmsOrderState.NEW.handleEvent(
            orderQuantity,
            TraderWorkingEvent(newTradeId(), BigDecimal.TEN)
        )

        assertAll(
            { assertEquals(updatedOrderQuantity, orderQuantity) },
            { assertEquals(OmsOrderState.NEW, updatedState) }
        )
    }

    @Test
    fun `new state handling invalid working amount`() {
        val orderQuantity = OrderQuantity(BigDecimal.TEN, workedQuantity = BigDecimal.TWO)
        val exception = assertThrows<IllegalStateException> { OmsOrderState.NEW.handleEvent(
            orderQuantity,
            TraderExecutedEvent(newTradeId(), BigDecimal.TEN)
        ) }

        assertEquals("NEW Data should have ZERO used quantity", exception.message)

    }

    @Test
    fun `new state handling invalid executed amount`() {
        val orderQuantity = OrderQuantity(BigDecimal.TEN, executedQuantity = BigDecimal.TWO)
        val exception = assertThrows<IllegalStateException> { OmsOrderState.NEW.handleEvent(
            orderQuantity,
            TraderExecutedEvent(newTradeId(), BigDecimal.TEN)
        ) }

        assertEquals("NEW Data should have ZERO used quantity", exception.message)
    }

    @Test
    fun `new state handling invalid cancelled amount`() {
        val orderQuantity = OrderQuantity(BigDecimal.TEN, cancelledQuantity = CancelledQuantity(BigDecimal.TWO))
        val exception = assertThrows<IllegalStateException> { OmsOrderState.NEW.handleEvent(
            orderQuantity,
            TraderExecutedEvent(newTradeId(), BigDecimal.TEN)
        ) }

        assertEquals("NEW Data should have ZERO used quantity", exception.message)
    }

    @Test
    fun `can transition from worked to executed`() {
        val orderQuantity = OrderQuantity(BigDecimal.TEN, workedQuantity = BigDecimal.TEN)
        val (updatedOrderQuantity, updatedState) = OmsOrderState.WORKED.handleEvent(
            orderQuantity,
            TraderExecutedEvent(newTradeId(), BigDecimal.TEN)
        )

        assertAll(
            { assertEquals(BigDecimal.TEN, updatedOrderQuantity.totalQuantity) { "Total Quantity should be equal 10" } },
            { assertEquals(BigDecimal.ZERO, updatedOrderQuantity.openQuantity) { "Open Quantity should be equal 0" } },
            { assertEquals(BigDecimal.ZERO, updatedOrderQuantity.workedQuantity) { "Worked Quantity should be equal 0" } },
            { assertEquals(BigDecimal.TEN, updatedOrderQuantity.executedQuantity) { "Executed Quantity should be equal 10" } },
            { assertEquals(CancelledQuantity(BigDecimal.ZERO), updatedOrderQuantity.cancelledQuantity) { "Cancelled Quantity should be equal 0" } },
            { assertEquals(BigDecimal.TEN, updatedOrderQuantity.usedQuantity) { "Used Quantity should be equal 10" } },
            { assertEquals(OmsOrderState.EXECUTED, updatedState) }
        )
    }
}