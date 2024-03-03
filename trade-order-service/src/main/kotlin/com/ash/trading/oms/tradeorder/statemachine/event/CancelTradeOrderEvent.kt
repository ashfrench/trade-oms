package com.ash.trading.oms.tradeorder.statemachine.event

import java.time.LocalDateTime

data class CancelTradeOrderEvent(override val payload: CancelTradeOrderPayload): OmsTradeOrderEvent<CancelTradeOrderPayload>
data class CancelTradeOrderPayload(val cancelledTime: LocalDateTime)