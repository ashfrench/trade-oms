package com.ash.trading.oms.tradeorder.statemachine

import com.ash.trading.oms.model.CancelledQuantity
import com.ash.trading.oms.model.TradeOrderQuantities
import com.ash.trading.oms.model.newOrderId
import com.ash.trading.oms.model.newTradeId
import com.ash.trading.oms.tradeorder.statemachine.event.AddTradeToTradeOrderEvent
import com.ash.trading.oms.tradeorder.statemachine.event.RemoveTradeFromTradeOrderEvent
import com.ash.trading.oms.tradeorder.statemachine.event.UpdateTradeForTradeOrderEvent
import org.junit.jupiter.api.Assertions.assertAll
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

    @Test
    fun `can handle not being only partially executed state correctly`() {
        val tradeOrderQuantities = TradeOrderQuantities(mapOf(newOrderId() to BigDecimal.TEN), mapOf(newTradeId() to BigDecimal.ONE))

        val exception = assertThrows<IllegalStateException>("Invalid order type") {
            OmsTradeOrderState.EXECUTED.handleEvent(
                tradeOrderQuantities,
                AddTradeToTradeOrderEvent(newTradeId(), BigDecimal.ONE)
            )
        }

        assertEquals("EXECUTED Data should have ZERO open quantity and executed quantity GREATER than ZERO", exception.message)
    }

    @Test
    fun `update executed trade to partially executed`() {
        val tradeId = newTradeId()
        val tradeOrderQuantities = TradeOrderQuantities(mapOf(newOrderId() to BigDecimal.TEN), mapOf(tradeId to BigDecimal.TEN))
        val (updatedOrderQuantity, updatedState) = OmsTradeOrderState.EXECUTED.handleEvent(
            tradeOrderQuantities,
            UpdateTradeForTradeOrderEvent(tradeId, BigDecimal.TWO)
        )

        assertAll(
            { assertEquals(BigDecimal.TEN, updatedOrderQuantity.totalQuantity) { "Total Quantity should be equal 10" } },
            { assertEquals(BigDecimal(8), updatedOrderQuantity.openQuantity) { "Open Quantity should be equal 9" } },
            { assertEquals(BigDecimal.TWO, updatedOrderQuantity.executedQuantity) { "Executed Quantity should be equal 1" } },
            { assertEquals(CancelledQuantity(BigDecimal.ZERO), updatedOrderQuantity.cancelledQuantity) { "Cancelled Quantity should be equal 0" } },
            { assertEquals(BigDecimal.TWO, updatedOrderQuantity.usedQuantity) { "Used Quantity should be equal 1" } },
            { assertEquals(OmsTradeOrderState.PARTIALLY_EXECUTED, updatedState) }
        )
    }

    @Test
    fun `cannot update executed trade with invalid value`() {
        val tradeId = newTradeId()
        val tradeOrderQuantities = TradeOrderQuantities(mapOf(newOrderId() to BigDecimal.TWO), mapOf(tradeId to BigDecimal.TWO))
        val (updatedOrderQuantity, updatedState) = OmsTradeOrderState.EXECUTED.handleEvent(
            tradeOrderQuantities,
            UpdateTradeForTradeOrderEvent(tradeId, BigDecimal.TEN)
        )

        assertAll(
            { assertEquals(tradeOrderQuantities, updatedOrderQuantity) },
            { assertEquals(OmsTradeOrderState.EXECUTED, updatedState) }
        )
    }

    @Test
    fun `can move to partially executed back to when removing a trade and leaving remaining trades`() {
        val tradeId = newTradeId()
        val tradeOrderQuantities = TradeOrderQuantities(mapOf(newOrderId() to BigDecimal.TEN), mapOf(tradeId to BigDecimal(9), newTradeId() to BigDecimal.ONE))
        val (updatedOrderQuantity, updatedState) = OmsTradeOrderState.EXECUTED.handleEvent(
            tradeOrderQuantities,
            RemoveTradeFromTradeOrderEvent(tradeId)
        )

        assertAll(
            { assertEquals(BigDecimal.TEN, updatedOrderQuantity.totalQuantity) { "Total Quantity should be equal 10" } },
            { assertEquals(BigDecimal(9), updatedOrderQuantity.openQuantity) { "Open Quantity should be equal 9" } },
            { assertEquals(BigDecimal.ONE, updatedOrderQuantity.executedQuantity) { "Executed Quantity should be equal 1" } },
            { assertEquals(CancelledQuantity(BigDecimal.ZERO), updatedOrderQuantity.cancelledQuantity) { "Cancelled Quantity should be equal 0" } },
            { assertEquals(BigDecimal.ONE, updatedOrderQuantity.usedQuantity) { "Used Quantity should be equal 1" } },
            { assertEquals(OmsTradeOrderState.PARTIALLY_EXECUTED, updatedState) }
        )
    }
    @Test
    fun `can transition from executed back to new when removing a trade`() {
        val tradeId = newTradeId()
        val tradeOrderQuantities = TradeOrderQuantities(mapOf(newOrderId() to BigDecimal.TEN), mapOf(tradeId to BigDecimal.TEN))
        val (updatedOrderQuantity, updatedState) = OmsTradeOrderState.EXECUTED.handleEvent(
            tradeOrderQuantities,
            RemoveTradeFromTradeOrderEvent(tradeId)
        )

        assertAll(
            { assertEquals(BigDecimal.TEN, updatedOrderQuantity.totalQuantity) { "Total Quantity should be equal 10" } },
            { assertEquals(BigDecimal.TEN, updatedOrderQuantity.openQuantity) { "Open Quantity should be equal 10" } },
            { assertEquals(BigDecimal.ZERO, updatedOrderQuantity.executedQuantity) { "Executed Quantity should be equal 0" } },
            { assertEquals(CancelledQuantity(BigDecimal.ZERO), updatedOrderQuantity.cancelledQuantity) { "Cancelled Quantity should be equal 0" } },
            { assertEquals(BigDecimal.ZERO, updatedOrderQuantity.usedQuantity) { "Used Quantity should be equal 0" } },
            { assertEquals(OmsTradeOrderState.NEW, updatedState) }
        )
    }

    @Test
    fun `removing a trade which does not exist`() {
        val tradeOrderQuantities = TradeOrderQuantities(mapOf(newOrderId() to BigDecimal.TEN), mapOf(newTradeId() to BigDecimal.TEN))
        val (updatedOrderQuantity, updatedState) = OmsTradeOrderState.EXECUTED.handleEvent(
            tradeOrderQuantities,
            RemoveTradeFromTradeOrderEvent(newTradeId())
        )

        assertAll(
            { assertEquals(tradeOrderQuantities, updatedOrderQuantity) },
            { assertEquals(OmsTradeOrderState.EXECUTED, updatedState) }
        )
    }

    @Test
    fun `updating a trade which does not exist`() {
        val tradeOrderQuantities = TradeOrderQuantities(mapOf(newOrderId() to BigDecimal.TEN), mapOf(newTradeId() to BigDecimal.TEN))
        val (updatedOrderQuantity, updatedState) = OmsTradeOrderState.EXECUTED.handleEvent(
            tradeOrderQuantities,
            UpdateTradeForTradeOrderEvent(newTradeId(), BigDecimal.TWO)
        )

        assertAll(
            { assertEquals(tradeOrderQuantities, updatedOrderQuantity) },
            { assertEquals(OmsTradeOrderState.EXECUTED, updatedState) }
        )
    }

    @Test
    fun `updating a trade with negative value`() {
        val tradeId = newTradeId()
        val tradeOrderQuantities = TradeOrderQuantities(mapOf(newOrderId() to BigDecimal.TEN), mapOf(tradeId to BigDecimal.TEN))
        val (updatedOrderQuantity, updatedState) = OmsTradeOrderState.EXECUTED.handleEvent(
            tradeOrderQuantities,
            UpdateTradeForTradeOrderEvent(tradeId, BigDecimal(-1))
        )

        assertAll(
            { assertEquals(tradeOrderQuantities, updatedOrderQuantity) },
            { assertEquals(OmsTradeOrderState.EXECUTED, updatedState) }
        )
    }

    @Test
    fun `can handle unsupported event`() {
        val tradeOrderQuantities = TradeOrderQuantities(mapOf(newOrderId() to BigDecimal.TEN), mapOf(newTradeId() to BigDecimal.TEN))
        val (updatedOrderQuantity, updatedState) = OmsTradeOrderState.EXECUTED.handleEvent(
            tradeOrderQuantities,
            AddTradeToTradeOrderEvent(newTradeId(), BigDecimal.TWO)
        )

        assertAll(
            { assertEquals(tradeOrderQuantities, updatedOrderQuantity) },
            { assertEquals(OmsTradeOrderState.EXECUTED, updatedState) }
        )
    }

}