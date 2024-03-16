package com.ash.trading.oms.tradeorder.statemachine.handlers

import com.ash.trading.oms.model.TradeOrderQuantities
import com.ash.trading.oms.tradeorder.statemachine.OmsTradeOrderState
import com.ash.trading.oms.tradeorder.statemachine.event.*
import org.slf4j.LoggerFactory

object OmsTradeOrderPartiallyExecutedStateHandler {

    private val logger = LoggerFactory.getLogger(OmsTradeOrderPartiallyExecutedStateHandler::class.java)

    fun handleEvent(data: TradeOrderQuantities, event: OmsTradeOrderEvent): Pair<TradeOrderQuantities, OmsTradeOrderState> {
        try {
            when(event) {
                is AddTradeToTradeOrderEvent -> TODO()
                is CancelTradeOrderEvent -> TODO()
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

}