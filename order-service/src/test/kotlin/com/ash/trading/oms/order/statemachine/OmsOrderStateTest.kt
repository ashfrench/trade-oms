package com.ash.trading.oms.order.statemachine

import com.ash.trading.oms.model.CancelledQuantity
import com.ash.trading.oms.model.OrderQuantity
import com.ash.trading.oms.model.newTradeId
import com.ash.trading.oms.order.statemachine.events.*
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime

class OmsOrderStateTest {

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
    fun `can transition from partially executed to executed`() {
        val orderQuantity = OrderQuantity(BigDecimal.TEN, workedQuantity = BigDecimal(8), executedQuantity = BigDecimal.TWO)
        val (updatedOrderQuantity, updatedState) = OmsOrderState.PARTIALLY_EXECUTED.handleEvent(
            orderQuantity,
            TraderExecutedEvent(newTradeId(), BigDecimal(8))
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
    fun `can handle invalid transition from partially executed`() {
        val orderQuantity = OrderQuantity(BigDecimal.TEN, workedQuantity = BigDecimal(8), executedQuantity = BigDecimal.TWO)
        val (updatedOrderQuantity, updatedState) = OmsOrderState.PARTIALLY_EXECUTED.handleEvent(
            orderQuantity,
            TraderWorkingEvent(newTradeId(), BigDecimal.TEN)
        )

        assertAll(
            { assertEquals(updatedOrderQuantity, orderQuantity) },
            { assertEquals(OmsOrderState.PARTIALLY_EXECUTED, updatedState) }
        )
    }

    @Test
    fun `can handle error in transition from partially executed`() {
        val orderQuantity = OrderQuantity(BigDecimal.TEN, workedQuantity = BigDecimal(8), executedQuantity = BigDecimal.TWO)
        val (updatedOrderQuantity, updatedState) = OmsOrderState.PARTIALLY_EXECUTED.handleEvent(
            orderQuantity,
            TraderExecutedEvent(newTradeId(), BigDecimal.TEN)
        )

        assertAll(
            { assertEquals(updatedOrderQuantity, orderQuantity) },
            { assertEquals(OmsOrderState.PARTIALLY_EXECUTED, updatedState) }
        )
    }

    @Test
    fun `can transition from partially executed to partially executed`() {
        val orderQuantity = OrderQuantity(BigDecimal.TEN, workedQuantity = BigDecimal(8), executedQuantity = BigDecimal.TWO)
        val (updatedOrderQuantity, updatedState) = OmsOrderState.PARTIALLY_EXECUTED.handleEvent(
            orderQuantity,
            TraderExecutedEvent(newTradeId(), BigDecimal(7))
        )

        assertAll(
            { assertEquals(BigDecimal.TEN, updatedOrderQuantity.totalQuantity) { "Total Quantity should be equal 10" } },
            { assertEquals(BigDecimal.ZERO, updatedOrderQuantity.openQuantity) { "Open Quantity should be equal 0" } },
            { assertEquals(BigDecimal(1), updatedOrderQuantity.workedQuantity) { "Worked Quantity should be equal 1" } },
            { assertEquals(BigDecimal(9), updatedOrderQuantity.executedQuantity) { "Executed Quantity should be equal 9" } },
            { assertEquals(CancelledQuantity(BigDecimal.ZERO), updatedOrderQuantity.cancelledQuantity) { "Cancelled Quantity should be equal 0" } },
            { assertEquals(BigDecimal.TEN, updatedOrderQuantity.usedQuantity) { "Used Quantity should be equal 10" } },
            { assertEquals(OmsOrderState.PARTIALLY_EXECUTED, updatedState) }
        )
    }

    @Test
    fun `can transition from partially executed to cancelled`() {
        val orderQuantity = OrderQuantity(BigDecimal.TEN, workedQuantity = BigDecimal(8), executedQuantity = BigDecimal.TWO)
        val cancelledTime = LocalDateTime.now()
        val (updatedOrderQuantity, updatedState) = OmsOrderState.PARTIALLY_EXECUTED.handleEvent(
            orderQuantity,
            OrderCancelledEvent(cancelledTime)
        )

        assertAll(
            { assertEquals(BigDecimal.TEN, updatedOrderQuantity.totalQuantity) { "Total Quantity should be equal 10" } },
            { assertEquals(BigDecimal.ZERO, updatedOrderQuantity.openQuantity) { "Open Quantity should be equal 0" } },
            { assertEquals(BigDecimal.ZERO, updatedOrderQuantity.workedQuantity) { "Worked Quantity should be equal 0" } },
            { assertEquals(BigDecimal.TWO, updatedOrderQuantity.executedQuantity) { "Executed Quantity should be equal 2" } },
            { assertEquals(CancelledQuantity(BigDecimal(8), cancelledTime), updatedOrderQuantity.cancelledQuantity) { "Cancelled Quantity should be equal 8" } },
            { assertEquals(BigDecimal.TEN, updatedOrderQuantity.usedQuantity) { "Used Quantity should be equal 10" } },
            { assertEquals(OmsOrderState.CANCELLED, updatedState) }
        )
    }

    @Test
    fun `executed is final state and has no transitions`() {
        val orderQuantity = OrderQuantity(BigDecimal.TEN, executedQuantity = BigDecimal.TEN)
        val (updatedOrderQuantity, updatedState) = OmsOrderState.EXECUTED.handleEvent(
            orderQuantity,
            OrderCancelledEvent(LocalDateTime.now())
        )

        assertAll(
            { assertEquals(updatedOrderQuantity, orderQuantity) },
            { assertEquals(OmsOrderState.EXECUTED, updatedState) }
        )
    }

    @Test
    fun `cancelled is final state and has no transitions`() {
        val orderQuantity = OrderQuantity(BigDecimal.TEN, executedQuantity = BigDecimal.TEN)
        val (updatedOrderQuantity, updatedState) = OmsOrderState.CANCELLED.handleEvent(
            orderQuantity,
            OrderCancelledEvent(LocalDateTime.now())
        )

        assertAll(
            { assertEquals(updatedOrderQuantity, orderQuantity) },
            { assertEquals(OmsOrderState.CANCELLED, updatedState) }
        )
    }

}