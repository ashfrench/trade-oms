package com.ash.trading.oms.trade.events

import com.ash.trading.oms.model.TradeQuantity
import com.ash.trading.oms.trade.TradeQuantityState

interface TradeStateEventHandler {
    fun handleEvent(data: TradeQuantity, event: OmsTradeEvent): TradeQuantityState

}
