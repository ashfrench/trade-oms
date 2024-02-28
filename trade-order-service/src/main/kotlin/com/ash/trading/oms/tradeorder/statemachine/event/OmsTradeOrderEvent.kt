package com.ash.trading.oms.tradeorder.statemachine.event

import com.ash.trading.oms.statemachine.Event

sealed interface  OmsTradeOrderEvent<T>: Event<T>