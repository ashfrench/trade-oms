package com.ash.trading.oms.order.statemachine

import com.ash.trading.oms.model.ExecutedQuantity
import com.ash.trading.oms.model.OrderQuantity
import com.ash.trading.oms.model.newTradeId
import com.ash.trading.oms.order.statemachine.events.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime

class OmsOrderStateTest {

    @Test
    fun `can transition from new to worked`() {
        val orderQuantity = OrderQuantity(BigDecimal.TEN)
        val (updatedOrderQuantity, updatedState) = OmsOrderState.NEW.handleEvent(
            orderQuantity,
            TraderWorkingEvent(TraderWorkingPayload(newTradeId(), BigDecimal.TEN))
        )

        assertAll(
            { assertEquals(updatedOrderQuantity.totalQuantity, BigDecimal.TEN) { "Total Quantity should be equal 10" } },
            { assertEquals(updatedOrderQuantity.openQuantity, BigDecimal.ZERO) { "Open Quantity should be equal 0" } },
            { assertEquals(updatedOrderQuantity.workedQuantity, BigDecimal.TEN) { "Worked Quantity should be equal 0" } },
            { assertEquals(updatedOrderQuantity.executedQuantity, BigDecimal.ZERO) { "Executed Quantity should be equal 10" } },
            { assertEquals(updatedOrderQuantity.cancelledQuantity, BigDecimal.ZERO) { "Cancelled Quantity should be equal 0" } },
            { assertEquals(updatedOrderQuantity.usedQuantity, BigDecimal.TEN) { "Used Quantity should be equal 10" } },
            { assertEquals(updatedState, OmsOrderState.WORKED) }
        )
    }

    @Test
    fun `can transition from new to cancelled`() {
        val orderQuantity = OrderQuantity(BigDecimal.TEN)
        val (updatedOrderQuantity, updatedState) = OmsOrderState.NEW.handleEvent(
            orderQuantity,
            OrderCancelledEvent(OrderCancelledPayload(LocalDateTime.now()))
        )

        assertAll(
            { assertEquals(updatedOrderQuantity.totalQuantity, BigDecimal.TEN) { "Total Quantity should be equal 10" } },
            { assertEquals(updatedOrderQuantity.openQuantity, BigDecimal.ZERO) { "Open Quantity should be equal 0" } },
            { assertEquals(updatedOrderQuantity.workedQuantity, BigDecimal.ZERO) { "Worked Quantity should be equal 0" } },
            { assertEquals(updatedOrderQuantity.executedQuantity, BigDecimal.ZERO) { "Executed Quantity should be equal 0" } },
            { assertEquals(updatedOrderQuantity.cancelledQuantity, BigDecimal.TEN) { "Cancelled Quantity should be equal 10" } },
            { assertEquals(updatedOrderQuantity.usedQuantity, BigDecimal.TEN) { "Used Quantity should be equal 10" } },
            { assertEquals(updatedState, OmsOrderState.CANCELLED) }
        )
    }

    @Test
    fun `can handle no transition from new for executed event`() {
        val orderQuantity = OrderQuantity(BigDecimal.TEN)
        val (updatedOrderQuantity, updatedState) = OmsOrderState.NEW.handleEvent(
            orderQuantity,
            TraderExecutedEvent(TraderExecutedPayload(newTradeId(), BigDecimal.TEN))
        )

        assertAll(
            { assertEquals(orderQuantity, updatedOrderQuantity) },
            { assertEquals(updatedState, OmsOrderState.NEW) }
        )
    }

    @Test
    fun `can transition from worked to executed`() {
        val orderQuantity = OrderQuantity(BigDecimal.TEN, workedQuantity = BigDecimal.TEN)
        val (updatedOrderQuantity, updatedState) = OmsOrderState.WORKED.handleEvent(
            orderQuantity,
            TraderExecutedEvent(TraderExecutedPayload(newTradeId(), BigDecimal.TEN))
        )

        assertAll(
            { assertEquals(updatedOrderQuantity.totalQuantity, BigDecimal.TEN) { "Total Quantity should be equal 10" } },
            { assertEquals(updatedOrderQuantity.openQuantity, BigDecimal.ZERO) { "Open Quantity should be equal 0" } },
            { assertEquals(updatedOrderQuantity.workedQuantity, BigDecimal.ZERO) { "Worked Quantity should be equal 0" } },
            { assertEquals(updatedOrderQuantity.executedQuantity, BigDecimal.TEN) { "Executed Quantity should be equal 10" } },
            { assertEquals(updatedOrderQuantity.cancelledQuantity, BigDecimal.ZERO) { "Cancelled Quantity should be equal 0" } },
            { assertEquals(updatedOrderQuantity.usedQuantity, BigDecimal.TEN) { "Used Quantity should be equal 10" } },
            { assertEquals(updatedState, OmsOrderState.EXECUTED) }
        )
    }

    @Test
    fun `can transition from worked to partially executed`() {
        val orderQuantity = OrderQuantity(BigDecimal.TEN, workedQuantity = BigDecimal.TEN)
        val (updatedOrderQuantity, updatedState) = OmsOrderState.WORKED.handleEvent(
            orderQuantity,
            TraderExecutedEvent(TraderExecutedPayload(newTradeId(), BigDecimal.TWO))
        )

        assertAll(
            { assertEquals(updatedOrderQuantity.totalQuantity, BigDecimal.TEN) { "Total Quantity should be equal 10" } },
            { assertEquals(updatedOrderQuantity.openQuantity, BigDecimal.ZERO) { "Open Quantity should be equal 0" } },
            { assertEquals(updatedOrderQuantity.workedQuantity, BigDecimal(8)) { "Worked Quantity should be equal 8" } },
            { assertEquals(updatedOrderQuantity.executedQuantity, BigDecimal.TWO) { "Executed Quantity should be equal 2" } },
            { assertEquals(updatedOrderQuantity.cancelledQuantity, BigDecimal.ZERO) { "Cancelled Quantity should be equal 0" } },
            { assertEquals(updatedOrderQuantity.usedQuantity, BigDecimal.TEN) { "Used Quantity should be equal 10" } },
            { assertEquals(updatedState, OmsOrderState.PARTIALLY_EXECUTED) }
        )
    }

    @Test
    fun `can transition from worked to cancelled`() {
        val orderQuantity = OrderQuantity(BigDecimal.TEN, workedQuantity = BigDecimal.TEN)
        val (updatedOrderQuantity, updatedState) = OmsOrderState.WORKED.handleEvent(
            orderQuantity,
            OrderCancelledEvent(OrderCancelledPayload(LocalDateTime.now()))
        )

        assertAll(
            { assertEquals(updatedOrderQuantity.totalQuantity, BigDecimal.TEN) { "Total Quantity should be equal 10" } },
            { assertEquals(updatedOrderQuantity.openQuantity, BigDecimal.ZERO) { "Open Quantity should be equal 0" } },
            { assertEquals(updatedOrderQuantity.workedQuantity, BigDecimal.ZERO) { "Worked Quantity should be equal 0" } },
            { assertEquals(updatedOrderQuantity.executedQuantity, BigDecimal.ZERO) { "Executed Quantity should be equal 0" } },
            { assertEquals(updatedOrderQuantity.cancelledQuantity, BigDecimal.TEN) { "Cancelled Quantity should be equal 10" } },
            { assertEquals(updatedOrderQuantity.usedQuantity, BigDecimal.TEN) { "Used Quantity should be equal 10" } },
            { assertEquals(updatedState, OmsOrderState.CANCELLED) }
        )
    }

}