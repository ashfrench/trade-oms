package com.ash.trading.oms.model

import java.util.UUID

data class Trade(
    val tradeId: TradeId = newTradeId()
)

typealias TradeId = UUID
fun newTradeId() = UUID.randomUUID()