package com.ash.trading.oms.trade.events

import com.ash.trading.oms.model.TradeQuantity
import com.ash.trading.oms.trade.OmsTradeState
import com.ash.trading.oms.trade.TradeQuantityState
import org.slf4j.LoggerFactory

object OmsTradeNewStateHandler : TradeStateEventHandler {

    private val logger = LoggerFactory.getLogger(OmsTradeNewStateHandler::class.java)
    override fun handleEvent(data: TradeQuantity, event: OmsTradeEvent): TradeQuantityState {
        return try {
            when (event) {
                is OmsSendToEmsTradeEvent -> handleSendToEms(event)
                else -> {
                    logger.warn("NO IDEA $event")
                    TODO()
                }
            }
        } catch (e: Exception) {
            logger.error("Error Handling Event Type [${event.javaClass.simpleName}] from ${OmsTradeState.NEW} state", e)
            data to OmsTradeState.NEW
        }
    }

    private fun handleSendToEms(event: OmsSendToEmsTradeEvent): TradeQuantityState {
        TODO("Not yet implemented")
    }
}