package com.ash.trading.oms.tradeorder.statemachine

import com.ash.trading.oms.model.CancelledQuantity
import com.ash.trading.oms.model.TradeOrderQuantities
import com.ash.trading.oms.model.newOrderId
import com.ash.trading.oms.model.newTradeId
import com.ash.trading.oms.tradeorder.statemachine.event.*
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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

    @Test
    fun `can transition from partially executed back to new when removing a trade`() {
        val tradeId = newTradeId()
        val tradeOrderQuantities = TradeOrderQuantities(mapOf(newOrderId() to BigDecimal.TEN), mapOf(tradeId to BigDecimal.ONE))
        val (updatedOrderQuantity, updatedState) = OmsTradeOrderState.PARTIALLY_EXECUTED.handleEvent(
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
    fun `remove a trade that is not in the trade order`() {
        val tradeOrderQuantities = TradeOrderQuantities(mapOf(newOrderId() to BigDecimal.TEN), mapOf(newTradeId() to BigDecimal.ONE))
        val (updatedOrderQuantity, updatedState) = OmsTradeOrderState.PARTIALLY_EXECUTED.handleEvent(
            tradeOrderQuantities,
            RemoveTradeFromTradeOrderEvent(newTradeId())
        )

        assertAll(
            { assertEquals(tradeOrderQuantities, updatedOrderQuantity) },
            { assertEquals(OmsTradeOrderState.PARTIALLY_EXECUTED, updatedState) }
        )
    }

    @Test
    fun `can stay in partially executed back to when removing a trade and leaving remaining trades`() {
        val tradeId = newTradeId()
        val tradeOrderQuantities = TradeOrderQuantities(mapOf(newOrderId() to BigDecimal.TEN), mapOf(tradeId to BigDecimal.ONE, newTradeId() to BigDecimal.ONE))
        val (updatedOrderQuantity, updatedState) = OmsTradeOrderState.PARTIALLY_EXECUTED.handleEvent(
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
    fun `can transition from partially executed to cancelled`() {
        val tradeOrderQuantities = TradeOrderQuantities(mapOf(newOrderId() to BigDecimal.TEN), mapOf(newTradeId() to BigDecimal.ONE))
        val cancelledTime = LocalDateTime.now()
        val (updatedOrderQuantity, updatedState) = OmsTradeOrderState.PARTIALLY_EXECUTED.handleEvent(
            tradeOrderQuantities,
            CancelTradeOrderEvent(cancelledTime)
        )

        assertAll(
            { assertEquals(BigDecimal.TEN, updatedOrderQuantity.totalQuantity) { "Total Quantity should be equal 10" } },
            { assertEquals(BigDecimal.ZERO, updatedOrderQuantity.openQuantity) { "Open Quantity should be equal 0" } },
            { assertEquals(BigDecimal.ONE, updatedOrderQuantity.executedQuantity) { "Executed Quantity should be equal 1" } },
            { assertEquals(CancelledQuantity(BigDecimal(9), cancelledTime), updatedOrderQuantity.cancelledQuantity) { "Cancelled Quantity should be equal 9" } },
            { assertEquals(BigDecimal.TEN, updatedOrderQuantity.usedQuantity) { "Used Quantity should be equal 10" } },
            { assertEquals(OmsTradeOrderState.CANCELLED, updatedState) }
        )
    }

    @Test
    fun `unable to add same trade again in partially executed`() {
        val tradeId = newTradeId()
        val tradeOrderQuantities = TradeOrderQuantities(mapOf(newOrderId() to BigDecimal.TEN), mapOf(tradeId to BigDecimal.ONE))
        val (updatedOrderQuantity, updatedState) = OmsTradeOrderState.PARTIALLY_EXECUTED.handleEvent(
            tradeOrderQuantities,
            AddTradeToTradeOrderEvent(tradeId, BigDecimal(9))
        )

        assertAll(
            { assertEquals(tradeOrderQuantities, updatedOrderQuantity) },
            { assertEquals(OmsTradeOrderState.PARTIALLY_EXECUTED, updatedState) }
        )
    }

    @Test
    fun `update trade in partially executed`() {
        val tradeId = newTradeId()
        val tradeOrderQuantities = TradeOrderQuantities(mapOf(newOrderId() to BigDecimal.TEN), mapOf(tradeId to BigDecimal.ONE))
        val (updatedOrderQuantity, updatedState) = OmsTradeOrderState.PARTIALLY_EXECUTED.handleEvent(
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
    fun `update trade in partially executed with negative value`() {
        val tradeId = newTradeId()
        val tradeOrderQuantities = TradeOrderQuantities(mapOf(newOrderId() to BigDecimal.TEN), mapOf(tradeId to BigDecimal.ONE))
        val (updatedOrderQuantity, updatedState) = OmsTradeOrderState.PARTIALLY_EXECUTED.handleEvent(
            tradeOrderQuantities,
            UpdateTradeForTradeOrderEvent(tradeId, BigDecimal(-1))
        )

        assertAll(
            { assertEquals(tradeOrderQuantities, updatedOrderQuantity) },
            { assertEquals(OmsTradeOrderState.PARTIALLY_EXECUTED, updatedState) }
        )
    }

    @Test
    fun `update trade in partially executed to make fully executed`() {
        val tradeId = newTradeId()
        val tradeOrderQuantities = TradeOrderQuantities(mapOf(newOrderId() to BigDecimal.TEN), mapOf(tradeId to BigDecimal.ONE))
        val (updatedOrderQuantity, updatedState) = OmsTradeOrderState.PARTIALLY_EXECUTED.handleEvent(
            tradeOrderQuantities,
            UpdateTradeForTradeOrderEvent(tradeId, BigDecimal.TEN)
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

    @Test
    fun `can handle quantities that are not in partially executed`() {
        val tradeOrderQuantities = TradeOrderQuantities(mapOf(newOrderId() to BigDecimal.ONE))


        val exception = assertThrows<IllegalStateException>("Invalid order type") {
            OmsTradeOrderState.PARTIALLY_EXECUTED.handleEvent(
                tradeOrderQuantities,
                AddTradeToTradeOrderEvent(newTradeId(), BigDecimal.ONE)
            )
        }

        assertEquals("Partially Executed Data should have a positive used quantity and positive open quantity", exception.message)
    }

    @Test
    fun `can handle quantities with cancelled that are not in partially executed`() {
        val tradeOrderQuantities = TradeOrderQuantities(mapOf(newOrderId() to BigDecimal.ONE), cancelledQuantity = CancelledQuantity(BigDecimal.ONE))

        val exception = assertThrows<IllegalStateException>("Invalid order type") {
            OmsTradeOrderState.PARTIALLY_EXECUTED.handleEvent(
                tradeOrderQuantities,
                AddTradeToTradeOrderEvent(newTradeId(), BigDecimal.ONE)
            )
        }

        assertEquals("Partially Executed Data should have a positive used quantity and positive open quantity", exception.message)
    }

    @Test
    fun `can handle unsupported event`() {
        val tradeOrderQuantities = TradeOrderQuantities(mapOf(newOrderId() to BigDecimal.TEN), mapOf(newTradeId() to BigDecimal.ONE))
        val (updatedOrderQuantity, updatedState) = OmsTradeOrderState.PARTIALLY_EXECUTED.handleEvent(
            tradeOrderQuantities,
            AddOrderToTradeOrderEvent(newTradeId(), BigDecimal.TWO)
        )

        assertAll(
            { assertEquals(tradeOrderQuantities, updatedOrderQuantity) },
            { assertEquals(OmsTradeOrderState.PARTIALLY_EXECUTED, updatedState) }
        )
    }
}