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

class OmsOrderStateTest {

    @Test
    fun `cancelled is final state and has no transitions`() {
        val orderQuantity = OrderQuantity(BigDecimal.TEN, cancelledQuantity = CancelledQuantity(BigDecimal.TEN, LocalDateTime.now()))
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