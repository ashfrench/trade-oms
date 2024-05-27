package com.ash.trading.oms.tradeorder.statemachine.handlers

import com.ash.trading.oms.model.TradeOrderQuantities
import com.ash.trading.oms.tradeorder.statemachine.OmsTradeOrderState
import com.ash.trading.oms.tradeorder.statemachine.TradeOrderQuantitiesState
import com.ash.trading.oms.tradeorder.statemachine.event.CompletedTradeOrderEvent
import com.ash.trading.oms.tradeorder.statemachine.event.OmsTradeOrderEvent
import com.ash.trading.oms.tradeorder.statemachine.event.RemoveTradeFromTradeOrderEvent
import com.ash.trading.oms.tradeorder.statemachine.event.UpdateTradeForTradeOrderEvent
import org.slf4j.LoggerFactory
import java.math.BigDecimal

internal data object OmsTradeOrderExecutedStateHandler: TradeOrderStateEventHandler {

    private val logger = LoggerFactory.getLogger(OmsTradeOrderExecutedStateHandler::class.java)

    override fun handleEvent(data: TradeOrderQuantities, event: OmsTradeOrderEvent): TradeOrderQuantitiesState {
        return try {
            when(event) {
                is UpdateTradeForTradeOrderEvent -> handleUpdateTradeEvent(data, event)
                is RemoveTradeFromTradeOrderEvent -> handleRemoveTradeEvent(data, event)
                is CompletedTradeOrderEvent -> handleCompletedEvent(data, event)
                else -> {
                    logger.warn("Invalid Event Type [${event.javaClass.simpleName}] from [${OmsTradeOrderState.EXECUTED}] state")
                    data to OmsTradeOrderState.EXECUTED
                }
            }
        } catch (e: Exception) {
            logger.error("Error when handling Event Type [${event.javaClass.simpleName}] from [${OmsTradeOrderState.EXECUTED}] state", e)
            data to OmsTradeOrderState.EXECUTED
        }
    }

    private fun handleUpdateTradeEvent(data: TradeOrderQuantities, event: UpdateTradeForTradeOrderEvent): TradeOrderQuantitiesState {
        val updatedQuantities = data.tradeQuantities.toMutableMap()
        updatedQuantities.computeIfPresent(event.tradeId) { _,_ -> event.executedQuantity }

        val updatedData = data.copy(tradeQuantities = updatedQuantities)

        val updateState = if (updatedData.openQuantity == BigDecimal.ZERO) {
            OmsTradeOrderState.EXECUTED
        } else {
            OmsTradeOrderState.PARTIALLY_EXECUTED
        }

        return updatedData to updateState
    }

    private fun handleRemoveTradeEvent(data: TradeOrderQuantities, event: RemoveTradeFromTradeOrderEvent): TradeOrderQuantitiesState {
        if (!data.tradeQuantities.containsKey(event.tradeId)) {
            return data to OmsTradeOrderState.EXECUTED
        }

        val updatedQuantities = data.tradeQuantities.toMutableMap()
        updatedQuantities.remove(event.tradeId)

        val updatedData = data.copy(tradeQuantities = updatedQuantities)

        val updateState = if (updatedData.usedQuantity == BigDecimal.ZERO) {
            OmsTradeOrderState.NEW
        } else {
            OmsTradeOrderState.PARTIALLY_EXECUTED
        }

        return updatedData to updateState
    }

    private fun handleCompletedEvent(data: TradeOrderQuantities, event: CompletedTradeOrderEvent) =
        data.copy(completedTime = event.completedTime) to OmsTradeOrderState.COMPLETED

}