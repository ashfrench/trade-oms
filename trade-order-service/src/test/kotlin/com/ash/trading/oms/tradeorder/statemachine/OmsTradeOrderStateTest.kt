package com.ash.trading.oms.tradeorder.statemachine

import com.ash.trading.oms.model.TradeOrderQuantities
import com.ash.trading.oms.model.newOrderId
import com.ash.trading.oms.model.newTradeId
import com.ash.trading.oms.tradeorder.statemachine.event.AddOrderToTradeOrderEvent
import com.ash.trading.oms.tradeorder.statemachine.event.AddTradeToTradeOrderEvent
import com.ash.trading.oms.tradeorder.statemachine.event.CancelTradeOrderEvent
import com.ash.trading.oms.tradeorder.statemachine.event.UpdateOrderForTradeOrderEvent
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime

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
            { assertEquals(BigDecimal.ZERO, updatedOrderQuantity.cancelledQuantity) { "Cancelled Quantity should be equal 0" } },
            { assertEquals(BigDecimal.TWO, updatedOrderQuantity.usedQuantity) { "Used Quantity should be equal 10" } },
            { assertEquals(OmsTradeOrderState.PARTIALLY_EXECUTED, updatedState) }
        )
    }

    @Test
    fun `can transition from new to cancelled`() {
        val tradeOrderQuantities = TradeOrderQuantities(mapOf(newOrderId() to BigDecimal.TEN))
        val (updatedOrderQuantity, updatedState) = OmsTradeOrderState.NEW.handleEvent(
            tradeOrderQuantities,
            CancelTradeOrderEvent(LocalDateTime.now())
        )

        assertAll(
            { assertEquals(BigDecimal.TEN, updatedOrderQuantity.totalQuantity) { "Total Quantity should be equal 10" } },
            { assertEquals(BigDecimal.ZERO, updatedOrderQuantity.openQuantity) { "Open Quantity should be equal 8" } },
            { assertEquals(BigDecimal.ZERO, updatedOrderQuantity.executedQuantity) { "Executed Quantity should be equal 2" } },
            { assertEquals(BigDecimal.TEN, updatedOrderQuantity.cancelledQuantity) { "Cancelled Quantity should be equal 0" } },
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
            { assertEquals(BigDecimal.ZERO, updatedOrderQuantity.cancelledQuantity) { "Cancelled Quantity should be equal 0" } },
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
            { assertEquals(BigDecimal.ZERO, updatedOrderQuantity.cancelledQuantity) { "Cancelled Quantity should be equal 0" } },
            { assertEquals(BigDecimal.ZERO, updatedOrderQuantity.usedQuantity) { "Used Quantity should be equal 2" } },
            { assertEquals(BigDecimal.TWO, updatedOrderQuantity.orderQuantities[orderId]) { "Order Quantity should be equal 2" } },
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

}