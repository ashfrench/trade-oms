package com.ash.trading.oms.tradeorder.statemachine

enum class OmsTradeOrderState {

    NEW,
    PARTIALLY_EXECUTED,
    EXECUTED,
    CANCELLED,
    DELETED

}