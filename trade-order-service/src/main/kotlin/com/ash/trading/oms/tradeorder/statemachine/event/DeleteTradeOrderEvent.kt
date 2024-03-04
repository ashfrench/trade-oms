package com.ash.trading.oms.tradeorder.statemachine.event

import java.time.LocalDateTime

data class DeleteTradeOrderEvent(val cancelledTime: LocalDateTime): OmsTradeOrderEvent