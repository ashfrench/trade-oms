package com.ash.trading.oms.order.statemachine

import com.ash.trading.oms.model.CancelledQuantity
import com.ash.trading.oms.model.OrderQuantity
import com.ash.trading.oms.model.newTradeId
import com.ash.trading.oms.model.newUserId
import com.ash.trading.oms.order.statemachine.events.AdminFixEvent
import com.ash.trading.oms.order.statemachine.events.OrderCancelledEvent
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

}