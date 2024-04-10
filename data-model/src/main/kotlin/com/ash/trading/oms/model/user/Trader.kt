package com.ash.trading.oms.model.user

import com.ash.trading.oms.model.TraderId
import java.util.UUID

data class Trader(
    val traderId: TraderId,
    val traderName: String
)

