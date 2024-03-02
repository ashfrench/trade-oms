package com.ash.trading.oms.tradeorder.statemachine.handlers

import com.ash.trading.oms.model.OrderQuantity
import com.ash.trading.oms.tradeorder.statemachine.OmsTradeOrderState
import com.ash.trading.oms.tradeorder.statemachine.event.OmsTradeOrderEvent
import org.slf4j.LoggerFactory

class OmsTradeOrderTerminalStateHandler(private val state: OmsTradeOrderState) {

    private val logger = LoggerFactory.getLogger(OmsTradeOrderDeletedStateHandler::class.java)

    fun <T> handleEvent(data: OrderQuantity, event: OmsTradeOrderEvent<T>): Pair<OrderQuantity, OmsTradeOrderState> {
        logger.error("Invalid Event Type [${event.javaClass.simpleName}] from $state state")
        return data to state
    }
}