package com.ash.trading.oms.tradeorder.statemachine.handlers

import com.ash.trading.oms.model.TradeOrderQuantities
import com.ash.trading.oms.tradeorder.statemachine.OmsTradeOrderState
import com.ash.trading.oms.tradeorder.statemachine.event.AddTradeToTradeOrderEvent
import com.ash.trading.oms.tradeorder.statemachine.event.CancelTradeOrderEvent
import com.ash.trading.oms.tradeorder.statemachine.event.DeleteTradeOrderEvent
import com.ash.trading.oms.tradeorder.statemachine.event.OmsTradeOrderEvent
import org.slf4j.LoggerFactory

object OmsTradeOrderPartiallyExecutedStateHandler {

    private val logger = LoggerFactory.getLogger(OmsTradeOrderPartiallyExecutedStateHandler::class.java)

    fun handleEvent(data: TradeOrderQuantities, event: OmsTradeOrderEvent): Pair<TradeOrderQuantities, OmsTradeOrderState> {
        try {
            when(event) {
                is AddTradeToTradeOrderEvent -> TODO()
                is CancelTradeOrderEvent -> TODO()
                is DeleteTradeOrderEvent -> TODO()
            }
        } catch (e: Exception) {
            logger.error("Invalid Event Type [${event.javaClass.simpleName}] from ${OmsTradeOrderState.PARTIALLY_EXECUTED} state", e)
            return data to OmsTradeOrderState.PARTIALLY_EXECUTED
        }
    }

}