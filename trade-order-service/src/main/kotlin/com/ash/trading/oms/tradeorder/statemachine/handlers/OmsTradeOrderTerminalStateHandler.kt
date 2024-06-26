package com.ash.trading.oms.tradeorder.statemachine.handlers

import com.ash.trading.oms.model.TradeOrderQuantities
import com.ash.trading.oms.tradeorder.statemachine.OmsTradeOrderState
import com.ash.trading.oms.tradeorder.statemachine.TradeOrderQuantitiesState
import com.ash.trading.oms.tradeorder.statemachine.event.OmsTradeOrderEvent
import org.slf4j.LoggerFactory

internal class OmsTradeOrderTerminalStateHandler(private val state: OmsTradeOrderState): TradeOrderStateEventHandler {

    private val logger = LoggerFactory.getLogger(OmsTradeOrderTerminalStateHandler::class.java)

    override fun handleEvent(data: TradeOrderQuantities, event: OmsTradeOrderEvent): TradeOrderQuantitiesState {
        logger.warn("Invalid Event Type [${event.javaClass.simpleName}] from [$state] state")
        return data to state
    }
}