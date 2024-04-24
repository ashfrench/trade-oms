package com.ash.trading.oms.tradeorder.statemachine.handlers

import com.ash.trading.oms.model.TradeOrderQuantities
import com.ash.trading.oms.tradeorder.statemachine.OmsTradeOrderState
import com.ash.trading.oms.tradeorder.statemachine.TradeOrderQuantitiesState
import com.ash.trading.oms.tradeorder.statemachine.contains
import com.ash.trading.oms.tradeorder.statemachine.event.*
import org.slf4j.LoggerFactory

object OmsTradeOrderExecutedStateHandler {

    private val logger = LoggerFactory.getLogger(OmsTradeOrderExecutedStateHandler::class.java)

    fun handleEvent(data: TradeOrderQuantities, event: OmsTradeOrderEvent): TradeOrderQuantitiesState {
        check (data in OmsTradeOrderState.EXECUTED) {
            "${OmsTradeOrderState.EXECUTED} Data should have ZERO open quantity and executed quantity GREATER than ZERO"
        }

        return try {
            when(event) {
                is UpdateTradeForTradeOrderEvent -> handleUpdateTradeEvent(data, event)
                is RemoveTradeFromTradeOrderEvent -> handleRemoveTradeEvent(data, event)
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
        TODO("Not yet implemented")
    }

    private fun handleRemoveTradeEvent(data: TradeOrderQuantities, event: RemoveTradeFromTradeOrderEvent): TradeOrderQuantitiesState {
        TODO("Not yet implemented")
    }

}