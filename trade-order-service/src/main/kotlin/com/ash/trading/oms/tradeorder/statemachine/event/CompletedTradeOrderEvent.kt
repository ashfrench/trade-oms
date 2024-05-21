package com.ash.trading.oms.tradeorder.statemachine.event

import java.time.LocalDateTime

data class CompletedTradeOrderEvent(val completedTime: LocalDateTime): OmsTradeOrderEvent