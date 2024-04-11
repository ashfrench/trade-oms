package com.ash.trading.oms.order.statemachine

import com.ash.trading.oms.model.CancelledQuantity
import com.ash.trading.oms.model.OrderQuantity
import com.ash.trading.oms.model.newTradeId
import com.ash.trading.oms.model.newUserId
import com.ash.trading.oms.order.statemachine.events.AdminFixEvent
import com.ash.trading.oms.order.statemachine.events.TraderExecutedEvent
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.time.LocalDateTime

class OmsOrderErrorStateTest {

    @Test
    fun `can go from error to new`() {
        val orderQuantity = OrderQuantity(BigDecimal.TEN, executedQuantity = BigDecimal.TWO, cancelledQuantity = CancelledQuantity(BigDecimal.TWO, LocalDateTime.now()))

        val newData = OrderQuantity(BigDecimal.TEN)
        val (updatedOrderQuantity, updatedState) = OmsOrderState.ERROR.handleEvent(
            orderQuantity,
            AdminFixEvent(
                newUserId(),
                newData,
                OmsOrderState.NEW
            )
        )

        assertAll(
            { assertEquals(updatedOrderQuantity, newData) },
            { assertEquals(OmsOrderState.NEW, updatedState) }
        )
    }

    @Test
    fun `stays in error when data is not valid for updated state`() {
        val orderQuantity = OrderQuantity(BigDecimal.TEN, executedQuantity = BigDecimal.TWO, cancelledQuantity = CancelledQuantity(BigDecimal.TWO, LocalDateTime.now()))

        val newData = OrderQuantity(BigDecimal.TEN, executedQuantity = BigDecimal.TEN)
        val (updatedOrderQuantity, updatedState) = OmsOrderState.ERROR.handleEvent(
            orderQuantity,
            AdminFixEvent(
                newUserId(),
                newData,
                OmsOrderState.NEW
            )
        )

        assertAll(
            { assertEquals(orderQuantity, updatedOrderQuantity) },
            { assertEquals(OmsOrderState.ERROR, updatedState) }
        )
    }

    @Test
    fun `error state when has valid new data`() {
        val orderQuantity = OrderQuantity(BigDecimal.TEN)
        val exception = assertThrows<IllegalStateException> { OmsOrderState.ERROR.handleEvent(
            orderQuantity,
            TraderExecutedEvent(newTradeId(), BigDecimal.TEN)
        ) }

        assertEquals("ERROR data should not be valid for another state NEW", exception.message)
    }

    @Test
    fun `error state when has valid worked data`() {
        val orderQuantity = OrderQuantity(BigDecimal.TEN, workedQuantity = BigDecimal.TEN)
        val exception = assertThrows<IllegalStateException> { OmsOrderState.ERROR.handleEvent(
            orderQuantity,
            TraderExecutedEvent(newTradeId(), BigDecimal.TEN)
        ) }

        assertEquals("ERROR data should not be valid for another state WORKED", exception.message)
    }

    @Test
    fun `error state when has valid partially executed data`() {
        val orderQuantity = OrderQuantity(BigDecimal.TEN, workedQuantity = BigDecimal(8), executedQuantity = BigDecimal.TWO)
        val exception = assertThrows<IllegalStateException> { OmsOrderState.ERROR.handleEvent(
            orderQuantity,
            TraderExecutedEvent(newTradeId(), BigDecimal.TEN)
        ) }

        assertEquals("ERROR data should not be valid for another state PARTIALLY_EXECUTED", exception.message)
    }

    @Test
    fun `error state when has valid executed data`() {
        val orderQuantity = OrderQuantity(BigDecimal.TEN, executedQuantity = BigDecimal.TEN)
        val exception = assertThrows<IllegalStateException> { OmsOrderState.ERROR.handleEvent(
            orderQuantity,
            TraderExecutedEvent(newTradeId(), BigDecimal.TEN)
        ) }

        assertEquals("ERROR data should not be valid for another state EXECUTED", exception.message)
    }

    @Test
    fun `error state when has valid cancelled data`() {
        val orderQuantity = OrderQuantity(BigDecimal.TEN, cancelledQuantity = CancelledQuantity(BigDecimal.TEN, LocalDateTime.now()))
        val exception = assertThrows<IllegalStateException> { OmsOrderState.ERROR.handleEvent(
            orderQuantity,
            TraderExecutedEvent(newTradeId(), BigDecimal.TEN)
        ) }

        assertEquals("ERROR data should not be valid for another state CANCELLED", exception.message)
    }

}