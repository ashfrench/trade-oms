package com.ash.trading.oms.tradeorder.statemachine.handlers

import com.ash.trading.oms.model.CancelledQuantity
import com.ash.trading.oms.model.TradeOrderQuantities
import com.ash.trading.oms.tradeorder.statemachine.OmsTradeOrderState
import com.ash.trading.oms.tradeorder.statemachine.event.*
import org.slf4j.LoggerFactory
import java.math.BigDecimal

object OmsTradeOrderPartiallyExecutedStateHandler {

    private val logger = LoggerFactory.getLogger(OmsTradeOrderPartiallyExecutedStateHandler::class.java)

    fun handleEvent(data: TradeOrderQuantities, event: OmsTradeOrderEvent): Pair<TradeOrderQuantities, OmsTradeOrderState> {
        try {
            return when(event) {
                is AddTradeToTradeOrderEvent -> handleAddTrade(data, event)
                is CancelTradeOrderEvent -> handleCancelTrade(data, event)
                is RemoveTradeFromTradeOrderEvent -> TODO()
                is UpdateTradeForTradeOrderEvent -> TODO()
                else -> {
                    logger.error("Invalid Event Type [${event.javaClass.simpleName}] from [${OmsTradeOrderState.PARTIALLY_EXECUTED}] state")
                    return data to OmsTradeOrderState.PARTIALLY_EXECUTED
                }
            }
        } catch (e: Exception) {
            logger.error("Error when handling Event Type [${event.javaClass.simpleName}] from [${OmsTradeOrderState.PARTIALLY_EXECUTED}] state", e)
            return data to OmsTradeOrderState.PARTIALLY_EXECUTED
        }
    }

    private fun handleAddTrade(data: TradeOrderQuantities, event: AddTradeToTradeOrderEvent): Pair<TradeOrderQuantities, OmsTradeOrderState> {
        if (event.executedQuantity < BigDecimal.ZERO) {
            throw RuntimeException("Executed Quantity added must be a positive value")
        }

        val updatedQuantities = data.tradeQuantities.toMutableMap()
        updatedQuantities.compute(event.tradeId) { tradeId, originalQuantity ->
            if (originalQuantity != null) {
                throw RuntimeException("Duplicate Trade [$tradeId] added")
            }
            event.executedQuantity
        }

        val updatedData = data.copy(tradeQuantities = updatedQuantities)

        val updateState = if (updatedData.openQuantity == BigDecimal.ZERO) {
            OmsTradeOrderState.EXECUTED
        } else {
            OmsTradeOrderState.PARTIALLY_EXECUTED
        }

        return updatedData to updateState
    }

    private fun handleCancelTrade(data: TradeOrderQuantities, event: CancelTradeOrderEvent): Pair<TradeOrderQuantities, OmsTradeOrderState> {
        val updatedData = data.copy(cancelledQuantity = CancelledQuantity(data.openQuantity, event.cancelledTime))
        return updatedData to OmsTradeOrderState.CANCELLED
    }

}