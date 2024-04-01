package com.ash.trading.oms.tradeorder.statemachine.handlers

import com.ash.trading.oms.model.TradeOrderQuantities
import com.ash.trading.oms.tradeorder.statemachine.OmsTradeOrderState
import com.ash.trading.oms.tradeorder.statemachine.TradeOrderQuantitiesState
import com.ash.trading.oms.tradeorder.statemachine.event.*
import org.slf4j.LoggerFactory

object OmsTradeOrderExecutedStateHandler {

    private val logger = LoggerFactory.getLogger(OmsTradeOrderExecutedStateHandler::class.java)

    fun handleEvent(data: TradeOrderQuantities, event: OmsTradeOrderEvent): TradeOrderQuantitiesState {
        try {
            when(event) {
                is AddTradeToTradeOrderEvent -> TODO()
                is CancelTradeOrderEvent -> TODO()
                is AddOrderToTradeOrderEvent -> TODO()
                is UpdateTradeForTradeOrderEvent -> TODO()
                else -> {
                    logger.error("Invalid Event Type [${event.javaClass.simpleName}] from [${OmsTradeOrderState.EXECUTED}] state")
                    return data to OmsTradeOrderState.EXECUTED
                }
            }
        } catch (e: Exception) {
            logger.error("Error when handling Event Type [${event.javaClass.simpleName}] from [${OmsTradeOrderState.EXECUTED}] state", e)
            return data to OmsTradeOrderState.EXECUTED
        }
    }

}