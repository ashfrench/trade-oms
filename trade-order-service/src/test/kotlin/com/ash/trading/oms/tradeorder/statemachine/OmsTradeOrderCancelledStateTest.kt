package com.ash.trading.oms.tradeorder.statemachine

import com.ash.trading.oms.model.CancelledQuantity
import com.ash.trading.oms.model.TradeOrderQuantities
import com.ash.trading.oms.model.newOrderId
import com.ash.trading.oms.model.newTradeId
import com.ash.trading.oms.tradeorder.statemachine.event.AddOrderToTradeOrderEvent
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class OmsTradeOrderCancelledStateTest {

    @Test
    fun `handle cancelled state`() {
        val tradeOrderQuantities = TradeOrderQuantities(mapOf(newOrderId() to BigDecimal.TEN), mapOf(), CancelledQuantity(BigDecimal.TEN))
        val (updatedOrderQuantity, updatedState) = OmsTradeOrderState.CANCELLED.handleEvent(
            tradeOrderQuantities,
            AddOrderToTradeOrderEvent(newTradeId(), BigDecimal.TWO)
        )

        assertAll(
            { assertEquals(tradeOrderQuantities, updatedOrderQuantity) },
            { assertEquals(OmsTradeOrderState.CANCELLED, updatedState) }
        )
    }
}