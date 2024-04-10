package com.ash.trading.oms.model

import java.math.BigDecimal
import java.util.*


fun newOrderId(): OrderId = UUID.randomUUID()
fun newTradeId(): TradeId = UUID.randomUUID()
fun newTradeOrderId(): TradeOrderId = UUID.randomUUID()

operator fun CancelledQuantity.compareTo(bigDecimal: BigDecimal) = quantity.compareTo(bigDecimal)
operator fun BigDecimal.plus(cancelledQuantity: CancelledQuantity) = plus(cancelledQuantity.quantity)
operator fun BigDecimal.compareTo(cancelledQuantity: CancelledQuantity) = compareTo(cancelledQuantity.quantity)
