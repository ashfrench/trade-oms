package com.ash.trading.oms.tradeorder.statemachine

import com.ash.trading.oms.model.TradeOrderQuantities
import com.ash.trading.oms.model.newOrderId
import com.ash.trading.oms.model.newTradeId
import com.ash.trading.oms.tradeorder.statemachine.event.AddTradeToTradeOrderEvent
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class OmsTradeOrderStateTest {


    @Test
    fun `can transition from new to executed`() {
        val tradeOrderQuantities = TradeOrderQuantities(mapOf(newOrderId() to BigDecimal.TEN))
        val (updatedOrderQuantity, updatedState) = OmsTradeOrderState.NEW.handleEvent(
            tradeOrderQuantities,
            AddTradeToTradeOrderEvent(newTradeId(), BigDecimal.TEN)
        )

        assertAll(
            { assertEquals(updatedOrderQuantity.totalQuantity, BigDecimal.TEN) { "Total Quantity should be equal 10" } },
            { assertEquals(updatedOrderQuantity.openQuantity, BigDecimal.ZERO) { "Open Quantity should be equal 0" } },
            { assertEquals(updatedOrderQuantity.executedQuantity, BigDecimal.TEN) { "Executed Quantity should be equal 10" } },
            { assertEquals(updatedOrderQuantity.cancelledQuantity, BigDecimal.ZERO) { "Cancelled Quantity should be equal 0" } },
            { assertEquals(updatedOrderQuantity.usedQuantity, BigDecimal.TEN) { "Used Quantity should be equal 10" } },
            { assertEquals(updatedState, OmsTradeOrderState.EXECUTED) }
        )
    }
    
}