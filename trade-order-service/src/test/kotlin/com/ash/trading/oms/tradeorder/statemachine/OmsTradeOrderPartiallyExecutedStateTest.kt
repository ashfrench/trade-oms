package com.ash.trading.oms.tradeorder.statemachine

import com.ash.trading.oms.model.CancelledQuantity
import com.ash.trading.oms.model.TradeOrderQuantities
import com.ash.trading.oms.model.newOrderId
import com.ash.trading.oms.model.newTradeId
import com.ash.trading.oms.tradeorder.statemachine.event.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime

class OmsTradeOrderPartiallyExecutedStateTest {


    @Test
    fun `can transition from partially executed to executed`() {
        val tradeOrderQuantities = TradeOrderQuantities(mapOf(newOrderId() to BigDecimal.TEN), mapOf(newTradeId() to BigDecimal.ONE))
        val (updatedOrderQuantity, updatedState) = OmsTradeOrderState.PARTIALLY_EXECUTED.handleEvent(
            tradeOrderQuantities,
            AddTradeToTradeOrderEvent(newTradeId(), BigDecimal(9))
        )

        assertAll(
            { assertEquals(BigDecimal.TEN, updatedOrderQuantity.totalQuantity) { "Total Quantity should be equal 10" } },
            { assertEquals(BigDecimal.ZERO, updatedOrderQuantity.openQuantity) { "Open Quantity should be equal 0" } },
            { assertEquals(BigDecimal.TEN, updatedOrderQuantity.executedQuantity) { "Executed Quantity should be equal 10" } },
            { assertEquals(CancelledQuantity(BigDecimal.ZERO), updatedOrderQuantity.cancelledQuantity) { "Cancelled Quantity should be equal 0" } },
            { assertEquals(BigDecimal.TEN, updatedOrderQuantity.usedQuantity) { "Used Quantity should be equal 10" } },
            { assertEquals(OmsTradeOrderState.EXECUTED, updatedState) }
        )
    }

}