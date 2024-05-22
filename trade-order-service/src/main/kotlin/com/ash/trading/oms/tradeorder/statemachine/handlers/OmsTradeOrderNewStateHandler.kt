package com.ash.trading.oms.tradeorder.statemachine.handlers

import com.ash.trading.oms.model.CancelledQuantity
import com.ash.trading.oms.model.TradeOrderQuantities
import com.ash.trading.oms.tradeorder.statemachine.OmsTradeOrderState
import com.ash.trading.oms.tradeorder.statemachine.TradeOrderQuantitiesState
import com.ash.trading.oms.tradeorder.statemachine.contains
import com.ash.trading.oms.tradeorder.statemachine.event.*
import org.slf4j.LoggerFactory
import java.math.BigDecimal

object OmsTradeOrderNewStateHandler: TradeOrderStateEventHandler {

    private val logger = LoggerFactory.getLogger(OmsTradeOrderNewStateHandler::class.java)

    override fun handleEvent(data: TradeOrderQuantities, event: OmsTradeOrderEvent): TradeOrderQuantitiesState {
        try {
            return when (event) {
                is AddTradeToTradeOrderEvent -> handleAddTrade(data, event)
                is AddOrderToTradeOrderEvent -> handleAddOrderEvent(data, event)
                is CancelTradeOrderEvent -> handleCancelTrade(data, event)
                is RemoveOrderFromTradeOrderEvent -> handleRemoveOrderEvent(data, event)
                is UpdateOrderForTradeOrderEvent -> handleUpdateOrderEvent(data, event)
                else -> {
                    logger.warn("Invalid Event Type [${event.javaClass.simpleName}] from [${OmsTradeOrderState.NEW}] state")
                    return data to OmsTradeOrderState.NEW
                }
            }
        } catch (e: Exception) {
            logger.error("Error when handling Event Type [${event.javaClass.simpleName}] from [${OmsTradeOrderState.NEW}] state", e)
            return data to OmsTradeOrderState.NEW
        }
    }

    private fun handleAddTrade(data: TradeOrderQuantities, event: AddTradeToTradeOrderEvent): TradeOrderQuantitiesState {
        val updatedData = data.copy(tradeQuantities = mapOf(event.tradeId to event.executedQuantity))

        val updateState = if (updatedData.openQuantity == BigDecimal.ZERO) {
            OmsTradeOrderState.EXECUTED
        } else {
            OmsTradeOrderState.PARTIALLY_EXECUTED
        }

        return updatedData to updateState
    }

    private fun handleAddOrderEvent(data: TradeOrderQuantities, event: AddOrderToTradeOrderEvent): TradeOrderQuantitiesState {
        check(!data.orderQuantities.containsKey(event.orderId)) {
            "Must not add OrderId[${event.orderId}] that already exists in the Trade Order"
        }

        val updatedData = data.copy(
            orderQuantities = data.orderQuantities + mapOf(event.orderId to event.workedQuantity)
        )
        return updatedData to OmsTradeOrderState.NEW
    }

    private fun handleUpdateOrderEvent(data: TradeOrderQuantities, event: UpdateOrderForTradeOrderEvent): TradeOrderQuantitiesState {
        check(data.orderQuantities.containsKey(event.orderId)) {
            "Order ${event.orderId} is not in this Trade Order"
        }

        val updatedOrderQuantities = data.orderQuantities.toMutableMap()
        updatedOrderQuantities[event.orderId] = event.workedQuantity

        val updatedData = data.copy(orderQuantities = updatedOrderQuantities)
        return updatedData to OmsTradeOrderState.NEW
    }

    private fun handleRemoveOrderEvent(data: TradeOrderQuantities, event: RemoveOrderFromTradeOrderEvent): TradeOrderQuantitiesState {
        val updatedOrderQuantities = data.orderQuantities.toMutableMap()
        updatedOrderQuantities.remove(event.orderId)

        return if (updatedOrderQuantities.isEmpty()) {
            data to OmsTradeOrderState.DELETED
        } else {
            val updatedData = data.copy(
                orderQuantities = updatedOrderQuantities
            )
            updatedData to OmsTradeOrderState.NEW
        }
    }

    private fun handleCancelTrade(data: TradeOrderQuantities, event: CancelTradeOrderEvent): TradeOrderQuantitiesState {
        val updatedData = data.copy(cancelledQuantity = CancelledQuantity(data.openQuantity, event.cancelledTime))
        return updatedData to OmsTradeOrderState.CANCELLED
    }
}