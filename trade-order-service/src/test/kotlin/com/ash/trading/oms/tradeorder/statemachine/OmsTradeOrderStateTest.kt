package com.ash.trading.oms.tradeorder.statemachine

import com.ash.trading.oms.model.TradeOrderQuantities
import com.ash.trading.oms.model.newOrderId
import com.ash.trading.oms.model.newTradeId
import com.ash.trading.oms.tradeorder.statemachine.event.AddTradeToTradeOrderEvent
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
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
            { assertEquals(BigDecimal.TEN, updatedOrderQuantity.totalQuantity) { "Total Quantity should be equal 10" } },
            { assertEquals(BigDecimal.ZERO, updatedOrderQuantity.openQuantity) { "Open Quantity should be equal 0" } },
            { assertEquals(BigDecimal.TEN, updatedOrderQuantity.executedQuantity) { "Executed Quantity should be equal 10" } },
            { assertEquals(BigDecimal.ZERO, updatedOrderQuantity.cancelledQuantity) { "Cancelled Quantity should be equal 0" } },
            { assertEquals(BigDecimal.TEN, updatedOrderQuantity.usedQuantity) { "Used Quantity should be equal 10" } },
            { assertEquals(OmsTradeOrderState.EXECUTED, updatedState) }
        )
    }
    
}