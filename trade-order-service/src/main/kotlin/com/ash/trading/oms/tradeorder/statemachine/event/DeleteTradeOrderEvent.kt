package com.ash.trading.oms.tradeorder.statemachine.event

import java.time.LocalDateTime

data class DeleteTradeOrderEvent(override val payload: DeleteTradeOrderPayload): OmsTradeOrderEvent<DeleteTradeOrderPayload>
data class DeleteTradeOrderPayload(val cancelledTime: LocalDateTime)