package com.ash.trading.oms.tradeorder.statemachine.handlers

import com.ash.trading.oms.model.TradeOrderQuantities
import com.ash.trading.oms.tradeorder.statemachine.OmsTradeOrderState
import com.ash.trading.oms.tradeorder.statemachine.event.OmsTradeOrderEvent
import org.slf4j.LoggerFactory

class OmsTradeOrderTerminalStateHandler(private val state: OmsTradeOrderState) {

    private val logger = LoggerFactory.getLogger(OmsTradeOrderTerminalStateHandler::class.java)

    fun handleEvent(data: TradeOrderQuantities, event: OmsTradeOrderEvent): Pair<TradeOrderQuantities, OmsTradeOrderState> {
        logger.error("Invalid Event Type [${event.javaClass.simpleName}] from [$state] state")
        return data to state
    }
}