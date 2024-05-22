package com.ash.trading.oms.tradeorder.statemachine.handlers

import com.ash.trading.oms.model.TradeOrderQuantities
import com.ash.trading.oms.tradeorder.statemachine.TradeOrderQuantitiesState
import com.ash.trading.oms.tradeorder.statemachine.event.OmsTradeOrderEvent

sealed interface TradeOrderStateEventHandler {
    fun handleEvent(data: TradeOrderQuantities, event: OmsTradeOrderEvent): TradeOrderQuantitiesState

}