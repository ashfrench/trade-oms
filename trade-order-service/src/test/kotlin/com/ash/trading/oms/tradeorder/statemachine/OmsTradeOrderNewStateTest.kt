package com.ash.trading.oms.tradeorder.statemachine

import com.ash.trading.oms.model.CancelledQuantity
import com.ash.trading.oms.model.TradeOrderQuantities
import com.ash.trading.oms.model.newOrderId
import com.ash.trading.oms.model.newTradeId
import com.ash.trading.oms.tradeorder.statemachine.event.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.time.LocalDateTime

class OmsTradeOrderNewStateTest {


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
            { assertEquals(CancelledQuantity(BigDecimal.ZERO), updatedOrderQuantity.cancelledQuantity) { "Cancelled Quantity should be equal 0" } },
            { assertEquals(BigDecimal.TEN, updatedOrderQuantity.usedQuantity) { "Used Quantity should be equal 10" } },
            { assertEquals(OmsTradeOrderState.EXECUTED, updatedState) }
        )
    }

    @Test
    fun `new trades should have no used quantity`() {
        val tradeOrderQuantities = TradeOrderQuantities(mapOf(newOrderId() to BigDecimal.TEN), mapOf(newTradeId() to BigDecimal.ONE))

        val exception = assertThrows<IllegalStateException>("Invalid data") {
            OmsTradeOrderState.NEW.handleEvent(
                tradeOrderQuantities,
                AddTradeToTradeOrderEvent(newTradeId(), BigDecimal.ONE)
            )
        }

        assertEquals("NEW Data should have ZERO used quantity", exception.message)
    }

    @Test
    fun `can transition from new to partially executed`() {
        val tradeOrderQuantities = TradeOrderQuantities(mapOf(newOrderId() to BigDecimal.TEN))
        val (updatedOrderQuantity, updatedState) = OmsTradeOrderState.NEW.handleEvent(
            tradeOrderQuantities,
            AddTradeToTradeOrderEvent(newTradeId(), BigDecimal.TWO)
        )

        assertAll(
            { assertEquals(BigDecimal.TEN, updatedOrderQuantity.totalQuantity) { "Total Quantity should be equal 10" } },
            { assertEquals(BigDecimal(8), updatedOrderQuantity.openQuantity) { "Open Quantity should be equal 8" } },
            { assertEquals(BigDecimal.TWO, updatedOrderQuantity.executedQuantity) { "Executed Quantity should be equal 2" } },
            { assertEquals(CancelledQuantity(BigDecimal.ZERO), updatedOrderQuantity.cancelledQuantity) { "Cancelled Quantity should be equal 0" } },
            { assertEquals(BigDecimal.TWO, updatedOrderQuantity.usedQuantity) { "Used Quantity should be equal 10" } },
            { assertEquals(OmsTradeOrderState.PARTIALLY_EXECUTED, updatedState) }
        )
    }

    @Test
    fun `can transition from new to cancelled`() {
        val tradeOrderQuantities = TradeOrderQuantities(mapOf(newOrderId() to BigDecimal.TEN))
        val cancelledTime = LocalDateTime.now()
        val (updatedOrderQuantity, updatedState) = OmsTradeOrderState.NEW.handleEvent(
            tradeOrderQuantities,
            CancelTradeOrderEvent(cancelledTime)
        )

        assertAll(
            { assertEquals(BigDecimal.TEN, updatedOrderQuantity.totalQuantity) { "Total Quantity should be equal 10" } },
            { assertEquals(BigDecimal.ZERO, updatedOrderQuantity.openQuantity) { "Open Quantity should be equal 8" } },
            { assertEquals(BigDecimal.ZERO, updatedOrderQuantity.executedQuantity) { "Executed Quantity should be equal 2" } },
            { assertEquals(CancelledQuantity(BigDecimal.TEN, cancelledTime), updatedOrderQuantity.cancelledQuantity) { "Cancelled Quantity should be equal 0" } },
            { assertEquals(BigDecimal.TEN, updatedOrderQuantity.usedQuantity) { "Used Quantity should be equal 10" } },
            { assertEquals(OmsTradeOrderState.CANCELLED, updatedState) }
        )
    }

    @Test
    fun `can add new order to trade order`() {
        val tradeOrderQuantities = TradeOrderQuantities(mapOf(newOrderId() to BigDecimal.ONE))
        val (updatedOrderQuantity, updatedState) = OmsTradeOrderState.NEW.handleEvent(
            tradeOrderQuantities,
            AddOrderToTradeOrderEvent(newOrderId(), BigDecimal.ONE)
        )

        assertAll(
            { assertEquals(BigDecimal.TWO, updatedOrderQuantity.totalQuantity) { "Total Quantity should be equal 2" } },
            { assertEquals(BigDecimal.TWO, updatedOrderQuantity.openQuantity) { "Open Quantity should be equal 0" } },
            { assertEquals(BigDecimal.ZERO, updatedOrderQuantity.executedQuantity) { "Executed Quantity should be equal 2" } },
            { assertEquals(CancelledQuantity(BigDecimal.ZERO), updatedOrderQuantity.cancelledQuantity) { "Cancelled Quantity should be equal 0" } },
            { assertEquals(BigDecimal.ZERO, updatedOrderQuantity.usedQuantity) { "Used Quantity should be equal 2" } },
            { assertEquals(OmsTradeOrderState.NEW, updatedState) }
        )
    }

    @Test
    fun `can update order to trade order`() {
        val orderId = newOrderId()
        val tradeOrderQuantities = TradeOrderQuantities(mapOf(orderId to BigDecimal.ONE))
        val (updatedOrderQuantity, updatedState) = OmsTradeOrderState.NEW.handleEvent(
            tradeOrderQuantities,
            UpdateOrderForTradeOrderEvent(orderId, BigDecimal.TWO)
        )

        assertAll(
            { assertEquals(BigDecimal.TWO, updatedOrderQuantity.totalQuantity) { "Total Quantity should be equal 2" } },
            { assertEquals(BigDecimal.TWO, updatedOrderQuantity.openQuantity) { "Open Quantity should be equal 0" } },
            { assertEquals(BigDecimal.ZERO, updatedOrderQuantity.executedQuantity) { "Executed Quantity should be equal 2" } },
            { assertEquals(CancelledQuantity(BigDecimal.ZERO), updatedOrderQuantity.cancelledQuantity) { "Cancelled Quantity should be equal 0" } },
            { assertEquals(BigDecimal.ZERO, updatedOrderQuantity.usedQuantity) { "Used Quantity should be equal 2" } },
            { assertEquals(BigDecimal.TWO, updatedOrderQuantity.orderQuantities[orderId]) { "Order Quantity should be equal 2" } },
            { assertEquals(OmsTradeOrderState.NEW, updatedState) }
        )
    }

    @Test
    fun `can remove order from trade order`() {
        val orderId = newOrderId()
        val tradeOrderQuantities = TradeOrderQuantities(mapOf(orderId to BigDecimal.ONE, newOrderId() to BigDecimal.ONE))
        val (updatedOrderQuantity, updatedState) = OmsTradeOrderState.NEW.handleEvent(
            tradeOrderQuantities,
            RemoveOrderFromTradeOrderEvent(orderId)
        )

        assertAll(
            { assertEquals(BigDecimal.ONE, updatedOrderQuantity.totalQuantity) { "Total Quantity should be equal 1" } },
            { assertEquals(BigDecimal.ONE, updatedOrderQuantity.openQuantity) { "Open Quantity should be equal 0" } },
            { assertEquals(BigDecimal.ZERO, updatedOrderQuantity.executedQuantity) { "Executed Quantity should be equal 1" } },
            { assertEquals(CancelledQuantity(BigDecimal.ZERO), updatedOrderQuantity.cancelledQuantity) { "Cancelled Quantity should be equal 0" } },
            { assertEquals(BigDecimal.ZERO, updatedOrderQuantity.usedQuantity) { "Used Quantity should be equal 1" } },
            { assertNull(updatedOrderQuantity.orderQuantities[orderId]) { "Order should be removed" } },
            { assertEquals(OmsTradeOrderState.NEW, updatedState) }
        )
    }

    @Test
    fun `can remove order from trade order leaving trade order empty`() {
        val orderId = newOrderId()
        val tradeOrderQuantities = TradeOrderQuantities(mapOf(orderId to BigDecimal.ONE))
        val (updatedOrderQuantity, updatedState) = OmsTradeOrderState.NEW.handleEvent(
            tradeOrderQuantities,
            RemoveOrderFromTradeOrderEvent(orderId)
        )

        assertAll(
            { assertEquals(tradeOrderQuantities, updatedOrderQuantity) },
            { assertEquals(OmsTradeOrderState.DELETED, updatedState) }
        )
    }

    @Test
    fun `new trade order has no trades to remove from trade order`() {
        val tradeOrderQuantities = TradeOrderQuantities(mapOf(newOrderId() to BigDecimal.ONE))
        val (updatedOrderQuantity, updatedState) = OmsTradeOrderState.NEW.handleEvent(
            tradeOrderQuantities,
            RemoveTradeFromTradeOrderEvent(newTradeId())
        )

        assertAll(
            { assertEquals(tradeOrderQuantities, updatedOrderQuantity) },
            { assertEquals(OmsTradeOrderState.NEW, updatedState) }
        )
    }

    @Test
    fun `new trade order has no trades to update for trade order`() {
        val tradeOrderQuantities = TradeOrderQuantities(mapOf(newOrderId() to BigDecimal.ONE))
        val (updatedOrderQuantity, updatedState) = OmsTradeOrderState.NEW.handleEvent(
            tradeOrderQuantities,
            UpdateTradeForTradeOrderEvent(newTradeId(), BigDecimal.ONE)
        )

        assertAll(
            { assertEquals(tradeOrderQuantities, updatedOrderQuantity) },
            { assertEquals(OmsTradeOrderState.NEW, updatedState) }
        )
    }

    @Test
    fun `remove order which does not exist has no effect on trade order`() {
        val orderId = newOrderId()
        val tradeOrderQuantities = TradeOrderQuantities(mapOf(newOrderId() to BigDecimal.ONE))
        val (updatedOrderQuantity, updatedState) = OmsTradeOrderState.NEW.handleEvent(
            tradeOrderQuantities,
            RemoveOrderFromTradeOrderEvent(orderId)
        )

        assertAll(
            { assertEquals(tradeOrderQuantities, updatedOrderQuantity) },
            { assertEquals(OmsTradeOrderState.NEW, updatedState) }
        )
    }

    @Test
    fun `invalid update order to trade order`() {
        val tradeOrderQuantities = TradeOrderQuantities(mapOf(newOrderId() to BigDecimal.ONE))
        val (updatedOrderQuantity, updatedState) = OmsTradeOrderState.NEW.handleEvent(
            tradeOrderQuantities,
            UpdateOrderForTradeOrderEvent(newOrderId(), BigDecimal.TEN)
        )

        assertAll(
            { assertEquals(tradeOrderQuantities, updatedOrderQuantity) },
            { assertEquals(OmsTradeOrderState.NEW, updatedState) }
        )
    }

    @Test
    fun `invalid add new order to trade order cannot add order twice`() {
        val orderId = newOrderId()
        val tradeOrderQuantities = TradeOrderQuantities(mapOf(orderId to BigDecimal.TWO))
        val (updatedOrderQuantity, updatedState) = OmsTradeOrderState.NEW.handleEvent(
            tradeOrderQuantities,
            AddOrderToTradeOrderEvent(orderId, BigDecimal.ONE)
        )

        assertAll(
            { assertEquals(BigDecimal.TWO, updatedOrderQuantity.totalQuantity) { "Total Quantity should be equal 2" } },
            { assertEquals(BigDecimal.TWO, updatedOrderQuantity.openQuantity) { "Open Quantity should be equal 0" } },
            { assertEquals(BigDecimal.ZERO, updatedOrderQuantity.executedQuantity) { "Executed Quantity should be equal 2" } },
            { assertEquals(CancelledQuantity(BigDecimal.ZERO), updatedOrderQuantity.cancelledQuantity) { "Cancelled Quantity should be equal 0" } },
            { assertEquals(BigDecimal.ZERO, updatedOrderQuantity.usedQuantity) { "Used Quantity should be equal 2" } },
            { assertEquals(OmsTradeOrderState.NEW, updatedState) }
        )
    }

    @Test
    fun `invalid trade leaves order in new`() {
        val tradeOrderQuantities = TradeOrderQuantities(mapOf(newOrderId() to BigDecimal.TWO))
        val (updatedOrderQuantity, updatedState) = OmsTradeOrderState.NEW.handleEvent(
            tradeOrderQuantities,
            AddTradeToTradeOrderEvent(newTradeId(), BigDecimal.TEN)
        )

        assertAll(
            { assertEquals(tradeOrderQuantities, updatedOrderQuantity) },
            { assertEquals(OmsTradeOrderState.NEW, updatedState) }
        )
    }

    @Test
    fun `invalid order leaves order in new`() {
        val tradeOrderQuantities = TradeOrderQuantities(mapOf(newOrderId() to BigDecimal.TWO))
        val (updatedOrderQuantity, updatedState) = OmsTradeOrderState.NEW.handleEvent(
                tradeOrderQuantities,
                AddTradeToTradeOrderEvent(newTradeId(), BigDecimal(-1))
        )

        assertAll(
                { assertEquals(tradeOrderQuantities, updatedOrderQuantity) },
                { assertEquals(OmsTradeOrderState.NEW, updatedState) }
        )
    }

    @Test
    fun `new state handling invalid traded amount`() {
        val tradeOrderQuantities = TradeOrderQuantities(mapOf(newOrderId() to BigDecimal.TWO), mapOf(newTradeId() to BigDecimal.ONE))
        val exception = assertThrows<IllegalStateException> { OmsTradeOrderState.NEW.handleEvent(
            tradeOrderQuantities,
            AddTradeToTradeOrderEvent(newTradeId(), BigDecimal.ONE)
        ) }

        assertEquals("NEW Data should have ZERO used quantity", exception.message)
    }

    @Test
    fun `new state handling invalid cancelled amount`() {
        val tradeOrderQuantities = TradeOrderQuantities(mapOf(newOrderId() to BigDecimal.TWO), cancelledQuantity = CancelledQuantity(BigDecimal.TWO))
        val exception = assertThrows<IllegalStateException> { OmsTradeOrderState.NEW.handleEvent(
            tradeOrderQuantities,
            AddTradeToTradeOrderEvent(newTradeId(), BigDecimal.ONE)
        ) }

        assertEquals("NEW Data should have ZERO used quantity", exception.message)
    }

}