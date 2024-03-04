package com.ash.trading.oms.tradeorder.statemachine.event

import java.time.LocalDateTime

data class CancelTradeOrderEvent(val cancelledTime: LocalDateTime): OmsTradeOrderEvent