package com.ash.trading.oms.tradeorder.statemachine

import com.ash.trading.oms.model.TradeOrderQuantities
import com.ash.trading.oms.model.newOrderId
import com.ash.trading.oms.model.newTradeId
import com.ash.trading.oms.tradeorder.statemachine.event.AddOrderToTradeOrderEvent
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.time.LocalDateTime

class OmsTradeOrderCompletedStateTest {

    @Test
    fun `handle completed state`() {
        val tradeOrderQuantities = TradeOrderQuantities(mapOf(newOrderId() to BigDecimal.TEN), mapOf(newTradeId() to BigDecimal.TEN), completedTime = LocalDateTime.now())
        val (updatedOrderQuantity, updatedState) = OmsTradeOrderState.COMPLETED.handleEvent(
            tradeOrderQuantities,
            AddOrderToTradeOrderEvent(newTradeId(), BigDecimal.TWO)
        )

        assertAll(
            { assertEquals(tradeOrderQuantities, updatedOrderQuantity) },
            { assertEquals(OmsTradeOrderState.COMPLETED, updatedState) }
        )
    }

    @Test
    fun `handle not in completed state`() {
        val tradeOrderQuantities = TradeOrderQuantities(mapOf(newOrderId() to BigDecimal.TEN), mapOf())
        val exception = assertThrows<IllegalStateException> { OmsTradeOrderState.COMPLETED.handleEvent(
            tradeOrderQuantities,
            AddOrderToTradeOrderEvent(newTradeId(), BigDecimal.TWO)
        ) }

        assertEquals("COMPLETED Data should have ZERO open quantity and Completed Time NOT NULL", exception.message)
    }
}