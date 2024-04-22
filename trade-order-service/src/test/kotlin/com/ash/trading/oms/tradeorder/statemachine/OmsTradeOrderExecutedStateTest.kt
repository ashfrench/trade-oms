package com.ash.trading.oms.tradeorder.statemachine

import com.ash.trading.oms.model.CancelledQuantity
import com.ash.trading.oms.model.TradeOrderQuantities
import com.ash.trading.oms.model.newOrderId
import com.ash.trading.oms.model.newTradeId
import com.ash.trading.oms.tradeorder.statemachine.event.AddTradeToTradeOrderEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal

class OmsTradeOrderExecutedStateTest {

    @Test
    fun `can handle not being in executed state correctly`() {
        val tradeOrderQuantities = TradeOrderQuantities(mapOf(newOrderId() to BigDecimal.ONE), cancelledQuantity = CancelledQuantity(BigDecimal.ONE))

        val exception = assertThrows<IllegalStateException>("Invalid order type") {
            OmsTradeOrderState.EXECUTED.handleEvent(
                tradeOrderQuantities,
                AddTradeToTradeOrderEvent(newTradeId(), BigDecimal.ONE)
            )
        }

        assertEquals("EXECUTED Data should have ZERO open quantity and executed quantity GREATER than ZERO", exception.message)
    }

}