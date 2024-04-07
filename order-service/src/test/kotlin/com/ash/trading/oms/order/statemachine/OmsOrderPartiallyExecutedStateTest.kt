package com.ash.trading.oms.order.statemachine

import com.ash.trading.oms.model.CancelledQuantity
import com.ash.trading.oms.model.OrderQuantity
import com.ash.trading.oms.model.newTradeId
import com.ash.trading.oms.order.statemachine.events.*
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.time.LocalDateTime

class OmsOrderPartiallyExecutedStateTest {

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

}