package com.ash.trading.oms.tradeorder.statemachine.handlers

import com.ash.trading.oms.model.TradeOrderQuantities
import com.ash.trading.oms.tradeorder.statemachine.OmsTradeOrderState
import com.ash.trading.oms.tradeorder.statemachine.event.AddTradeToTradeOrderEvent
import com.ash.trading.oms.tradeorder.statemachine.event.CancelTradeOrderEvent
import com.ash.trading.oms.tradeorder.statemachine.event.DeleteTradeOrderEvent
import com.ash.trading.oms.tradeorder.statemachine.event.OmsTradeOrderEvent
import org.slf4j.LoggerFactory
import java.math.BigDecimal

object OmsTradeOrderNewStateHandler {

    private val logger = LoggerFactory.getLogger(OmsTradeOrderNewStateHandler::class.java)

    fun handleEvent(data: TradeOrderQuantities, event: OmsTradeOrderEvent): Pair<TradeOrderQuantities, OmsTradeOrderState> {
        try {
            if(data.usedQuantity != BigDecimal.ZERO) {
                throw RuntimeException("New Data should have ZERO used quantity")
            }
            return when(event) {
                is AddTradeToTradeOrderEvent -> handleAddTrade(data, event)
                is CancelTradeOrderEvent -> TODO()
                is DeleteTradeOrderEvent -> TODO()
            }
        } catch (e: Exception) {
            logger.error("Invalid Event Type [${event.javaClass.simpleName}] from ${OmsTradeOrderState.NEW} state", e)
            return data to OmsTradeOrderState.NEW
        }
    }

    private fun handleAddTrade(data: TradeOrderQuantities, event: AddTradeToTradeOrderEvent): Pair<TradeOrderQuantities, OmsTradeOrderState> {
        val updatedData = data.copy(
            tradeQuantities = data.tradeQuantities + mapOf(event.tradeId to event.executedQuantity)
        )

        return updatedData to OmsTradeOrderState.EXECUTED
    }

}