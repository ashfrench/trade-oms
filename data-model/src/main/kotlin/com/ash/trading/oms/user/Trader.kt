package com.ash.trading.oms.user

import java.util.UUID

data class Trader(
    val traderId: TraderId,
    val traderName: String
)

typealias TraderId = UUID