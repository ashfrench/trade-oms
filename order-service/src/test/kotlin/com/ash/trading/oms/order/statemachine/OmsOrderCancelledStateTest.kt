package com.ash.trading.oms.order.statemachine

import com.ash.trading.oms.model.CancelledQuantity
import com.ash.trading.oms.model.OrderQuantity
import com.ash.trading.oms.model.newTradeId
import com.ash.trading.oms.order.statemachine.events.OrderCancelledEvent
import com.ash.trading.oms.order.statemachine.events.TraderExecutedEvent
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.time.LocalDateTime

class OmsOrderCancelledStateTest {

    @Test
    fun `cancelled is final state and has no transitions`() {
        val orderQuantity = OrderQuantity(BigDecimal.TEN, cancelledQuantity = CancelledQuantity(BigDecimal.TEN, LocalDateTime.now()))
        val (updatedOrderQuantity, updatedState) = OmsOrderState.CANCELLED.handleEvent(
            orderQuantity,
            OrderCancelledEvent()
        )

        assertAll(
            { assertEquals(updatedOrderQuantity, orderQuantity) },
            { assertEquals(OmsOrderState.CANCELLED, updatedState) }
        )
    }

    @Test
    fun `cancelled state handling invalid cancelled amount`() {
        val orderQuantity = OrderQuantity(BigDecimal.TEN)
        val exception = assertThrows<IllegalStateException> { OmsOrderState.CANCELLED.handleEvent(
            orderQuantity,
            TraderExecutedEvent(newTradeId(), BigDecimal.TWO)
        ) }

        assertEquals("CANCELLED data should have some cancelled quantity", exception.message)
    }

    @Test
    fun `cancelled state handling invalid cancelled amount and left over open quantity`() {
        val orderQuantity = OrderQuantity(BigDecimal.TEN, cancelledQuantity = CancelledQuantity(BigDecimal.TWO))
        val exception = assertThrows<IllegalStateException> { OmsOrderState.CANCELLED.handleEvent(
            orderQuantity,
            TraderExecutedEvent(newTradeId(), BigDecimal.TWO)
        ) }

        assertEquals("CANCELLED data should have some cancelled quantity", exception.message)
    }

    @Test
    fun `can be in cancelled state with partially executed orders`() {
        val orderQuantity = OrderQuantity(BigDecimal.TEN, cancelledQuantity = CancelledQuantity(BigDecimal.TWO), executedQuantity = BigDecimal(8))
        val (updatedOrderQuantity, updatedState) = OmsOrderState.CANCELLED.handleEvent(
            orderQuantity,
            OrderCancelledEvent()
        )

        assertAll(
            { assertEquals(updatedOrderQuantity, orderQuantity) },
            { assertEquals(OmsOrderState.CANCELLED, updatedState) }
        )
    }

}