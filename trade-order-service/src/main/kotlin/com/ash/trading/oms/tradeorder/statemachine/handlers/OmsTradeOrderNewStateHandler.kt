package com.ash.trading.oms.tradeorder.statemachine.handlers

import com.ash.trading.oms.model.OrderQuantity
import com.ash.trading.oms.tradeorder.statemachine.OmsTradeOrderState
import com.ash.trading.oms.tradeorder.statemachine.event.OmsTradeOrderEvent
import org.slf4j.LoggerFactory

object OmsTradeOrderNewStateHandler {

    private val logger = LoggerFactory.getLogger(OmsTradeOrderNewStateHandler::class.java)

    fun <T> handleEvent(data: OrderQuantity, event: OmsTradeOrderEvent<T>): Pair<OrderQuantity, OmsTradeOrderState> {
        try {
            TODO()
        } catch (e: Exception) {
            logger.error("Invalid Event Type [${event.javaClass.simpleName}] from ${OmsTradeOrderState.NEW} state", e)
            return data to OmsTradeOrderState.NEW
        }
    }

}