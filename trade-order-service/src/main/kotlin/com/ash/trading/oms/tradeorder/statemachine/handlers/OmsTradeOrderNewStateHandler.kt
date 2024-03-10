package com.ash.trading.oms.tradeorder.statemachine.handlers

import com.ash.trading.oms.model.TradeOrderQuantities
import com.ash.trading.oms.tradeorder.statemachine.OmsTradeOrderState
import com.ash.trading.oms.tradeorder.statemachine.event.*
import org.slf4j.LoggerFactory
import java.math.BigDecimal

object OmsTradeOrderNewStateHandler {

    private val logger = LoggerFactory.getLogger(OmsTradeOrderNewStateHandler::class.java)

    fun handleEvent(data: TradeOrderQuantities, event: OmsTradeOrderEvent): Pair<TradeOrderQuantities, OmsTradeOrderState> {
        try {
            if (data.usedQuantity != BigDecimal.ZERO) {
                throw RuntimeException("New Data should have ZERO used quantity")
            }
            return when (event) {
                is AddTradeToTradeOrderEvent -> handleAddTrade(data, event)
                is AddOrderToTradeOrderEvent -> handleAddOrderEvent(data, event)
                is CancelTradeOrderEvent -> handleCancelTrade(data, event)
                is RemoveOrderFromTradeOrderEvent -> TODO()
                is RemoveTradeFromTradeOrderEvent -> TODO()
                is UpdateOrderForTradeOrderEvent -> TODO()
            }
        } catch (e: Exception) {
            logger.error("Error when handling Event Type [${event.javaClass.simpleName}] from [${OmsTradeOrderState.NEW}] state", e)
            return data to OmsTradeOrderState.NEW
        }
    }

    private fun handleAddTrade(data: TradeOrderQuantities, event: AddTradeToTradeOrderEvent): Pair<TradeOrderQuantities, OmsTradeOrderState> {
        if (event.executedQuantity < BigDecimal.ZERO) {
            throw RuntimeException("Executed Quantity added must be a positive value")
        }
        val updatedData = data.copy(
            tradeQuantities = data.tradeQuantities + mapOf(event.tradeId to event.executedQuantity)
        )

        val updateState = if (updatedData.openQuantity == BigDecimal.ZERO) {
            OmsTradeOrderState.EXECUTED
        } else {
            OmsTradeOrderState.PARTIALLY_EXECUTED
        }

        return updatedData to updateState
    }

    private fun handleAddOrderEvent(data: TradeOrderQuantities, event: AddOrderToTradeOrderEvent): Pair<TradeOrderQuantities, OmsTradeOrderState> {
        if (event.workedQuantity < BigDecimal.ZERO) {
            throw RuntimeException("Worked Quantity added must be a positive value")
        }
        val updatedData = data.copy(
            orderQuantities = data.orderQuantities + mapOf(event.orderId to event.workedQuantity)
        )
        return updatedData to OmsTradeOrderState.NEW
    }

    private fun handleCancelTrade(data: TradeOrderQuantities, event: CancelTradeOrderEvent): Pair<TradeOrderQuantities, OmsTradeOrderState> {
        val updatedData = data.copy(cancelledQuantity = data.openQuantity)
        return updatedData to OmsTradeOrderState.CANCELLED
    }
}