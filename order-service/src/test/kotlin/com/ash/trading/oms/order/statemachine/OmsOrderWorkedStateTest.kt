package com.ash.trading.oms.order.statemachine

import com.ash.trading.oms.model.CancelledQuantity
import com.ash.trading.oms.model.OrderQuantity
import com.ash.trading.oms.model.newTradeId
import com.ash.trading.oms.model.newUserId
import com.ash.trading.oms.order.statemachine.events.AdminFixEvent
import com.ash.trading.oms.order.statemachine.events.OrderCancelledEvent
import com.ash.trading.oms.order.statemachine.events.TraderExecutedEvent
import com.ash.trading.oms.order.statemachine.events.TraderWorkingEvent
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.time.LocalDateTime

class OmsOrderWorkedStateTest {

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

    @Test
    fun `can transition from worked to worked`() {
        val orderQuantity = OrderQuantity(BigDecimal.TEN, workedQuantity = BigDecimal.TWO)
        val (updatedOrderQuantity, updatedState) = OmsOrderState.WORKED.handleEvent(
            orderQuantity,
            TraderWorkingEvent(newTradeId(), BigDecimal.TWO)
        )

        assertAll(
            { assertEquals(BigDecimal.TEN, updatedOrderQuantity.totalQuantity) { "Total Quantity should be equal 10" } },
            { assertEquals(BigDecimal(6), updatedOrderQuantity.openQuantity) { "Open Quantity should be equal 6" } },
            { assertEquals(BigDecimal(4), updatedOrderQuantity.workedQuantity) { "Worked Quantity should be equal 4" } },
            { assertEquals(BigDecimal.ZERO, updatedOrderQuantity.executedQuantity) { "Executed Quantity should be equal 10" } },
            { assertEquals(CancelledQuantity(BigDecimal.ZERO), updatedOrderQuantity.cancelledQuantity) { "Cancelled Quantity should be equal 0" } },
            { assertEquals(BigDecimal(4), updatedOrderQuantity.usedQuantity) { "Used Quantity should be equal 4" } },
            { assertEquals(OmsOrderState.WORKED, updatedState) }
        )
    }

    @Test
    fun `can handle invalid worked amount from worked`() {
        val orderQuantity = OrderQuantity(BigDecimal.TWO, workedQuantity = BigDecimal.TWO)
        val (updatedOrderQuantity, updatedState) = OmsOrderState.WORKED.handleEvent(
            orderQuantity,
            TraderWorkingEvent(newTradeId(), BigDecimal.TEN)
        )

        assertAll(
            { assertEquals(updatedOrderQuantity, orderQuantity) },
            { assertEquals(OmsOrderState.WORKED, updatedState) }
        )
    }

    @Test
    fun `can handle invalid event from worked`() {
        val orderQuantity = OrderQuantity(BigDecimal.TWO, workedQuantity = BigDecimal.TWO)
        val (updatedOrderQuantity, updatedState) = OmsOrderState.WORKED.handleEvent(
            orderQuantity,
            AdminFixEvent(
                newUserId(),
                orderQuantity,
                OmsOrderState.NEW
            )
        )

        assertAll(
            { assertEquals(updatedOrderQuantity, orderQuantity) },
            { assertEquals(OmsOrderState.WORKED, updatedState) }
        )
    }

    @Test
    fun `can transition from worked to partially executed`() {
        val orderQuantity = OrderQuantity(BigDecimal.TEN, workedQuantity = BigDecimal.TEN)
        val (updatedOrderQuantity, updatedState) = OmsOrderState.WORKED.handleEvent(
            orderQuantity,
            TraderExecutedEvent(newTradeId(), BigDecimal.TWO)
        )

        assertAll(
            { assertEquals(BigDecimal.TEN, updatedOrderQuantity.totalQuantity) { "Total Quantity should be equal 10" } },
            { assertEquals(BigDecimal.ZERO, updatedOrderQuantity.openQuantity) { "Open Quantity should be equal 0" } },
            { assertEquals(BigDecimal(8), updatedOrderQuantity.workedQuantity) { "Worked Quantity should be equal 8" } },
            { assertEquals(BigDecimal.TWO, updatedOrderQuantity.executedQuantity) { "Executed Quantity should be equal 2" } },
            { assertEquals(CancelledQuantity(BigDecimal.ZERO), updatedOrderQuantity.cancelledQuantity) { "Cancelled Quantity should be equal 0" } },
            { assertEquals(BigDecimal.TEN, updatedOrderQuantity.usedQuantity) { "Used Quantity should be equal 10" } },
            { assertEquals(OmsOrderState.PARTIALLY_EXECUTED, updatedState) }
        )
    }

    @Test
    fun `can transition from worked to cancelled`() {
        val orderQuantity = OrderQuantity(BigDecimal.TEN, workedQuantity = BigDecimal.TEN)
        val cancelledTime = LocalDateTime.now()
        val (updatedOrderQuantity, updatedState) = OmsOrderState.WORKED.handleEvent(
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
    fun `can transition from worked with open quantity to cancelled`() {
        val orderQuantity = OrderQuantity(BigDecimal.TEN, workedQuantity = BigDecimal.TWO)
        val cancelledTime = LocalDateTime.now()
        val (updatedOrderQuantity, updatedState) = OmsOrderState.WORKED.handleEvent(
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
    fun `worked state handling invalid working amount`() {
        val orderQuantity = OrderQuantity(BigDecimal.TEN)
        val exception = assertThrows<IllegalStateException> { OmsOrderState.WORKED.handleEvent(
            orderQuantity,
            TraderExecutedEvent(newTradeId(), BigDecimal.TWO)
        ) }

        assertEquals("WORKED Data should have worked quantity greater than ZERO and open quantity greater than ZERO", exception.message)

    }

    @Test
    fun `worked state handling invalid cancelled amount`() {
        val orderQuantity = OrderQuantity(
            BigDecimal.TEN,
            workedQuantity = BigDecimal.TWO,
            cancelledQuantity = CancelledQuantity(BigDecimal.TWO)
        )

        val exception = assertThrows<IllegalStateException> { OmsOrderState.WORKED.handleEvent(
            orderQuantity,
            TraderExecutedEvent(newTradeId(), BigDecimal.TWO)
        ) }

        assertEquals("WORKED Data should have worked quantity greater than ZERO and open quantity greater than ZERO", exception.message)

    }

    @Test
    fun `worked state handling invalid executed amount`() {
        val orderQuantity = OrderQuantity(
            BigDecimal.TEN,
            workedQuantity = BigDecimal.TWO,
            executedQuantity = BigDecimal.TWO
        )

        val exception = assertThrows<IllegalStateException> { OmsOrderState.WORKED.handleEvent(
            orderQuantity,
            TraderExecutedEvent(newTradeId(), BigDecimal.TWO)
        ) }

        assertEquals("WORKED Data should have worked quantity greater than ZERO and open quantity greater than ZERO", exception.message)

    }

}