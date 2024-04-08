package com.ash.trading.oms.order.statemachine

import com.ash.trading.oms.model.OrderQuantity
import com.ash.trading.oms.model.newTradeId
import com.ash.trading.oms.order.statemachine.events.OrderCancelledEvent
import com.ash.trading.oms.order.statemachine.events.TraderExecutedEvent
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal

class OmsOrderExecutedStateTest {

    @Test
    fun `executed is final state and has no transitions`() {
        val orderQuantity = OrderQuantity(BigDecimal.TEN, executedQuantity = BigDecimal.TEN)
        val (updatedOrderQuantity, updatedState) = OmsOrderState.EXECUTED.handleEvent(
            orderQuantity,
            OrderCancelledEvent()
        )

        assertAll(
            { assertEquals(updatedOrderQuantity, orderQuantity) },
            { assertEquals(OmsOrderState.EXECUTED, updatedState) }
        )
    }

    @Test
    fun `executed state handling no executed amount`() {
        val orderQuantity = OrderQuantity(BigDecimal.TEN)
        val exception = assertThrows<IllegalStateException> { OmsOrderState.EXECUTED.handleEvent(
            orderQuantity,
            TraderExecutedEvent(newTradeId(), BigDecimal.TWO)
        ) }

        assertEquals("EXECUTED data should have executed quantity equal to the total quantity", exception.message)

    }

    @Test
    fun `executed state handling invalid executed amount`() {
        val orderQuantity = OrderQuantity(BigDecimal.TEN, executedQuantity = BigDecimal.TWO, workedQuantity = BigDecimal(8))
        val exception = assertThrows<IllegalStateException> { OmsOrderState.EXECUTED.handleEvent(
            orderQuantity,
            TraderExecutedEvent(newTradeId(), BigDecimal.TWO)
        ) }

        assertEquals("EXECUTED data should have executed quantity equal to the total quantity", exception.message)
    }

}