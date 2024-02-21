package com.ash.trading.oms.model

enum class OrderState {
    NEW,
    ACKNOWLEDGED,
    EXECUTED,
    PARTIALLY_EXECUTED,
    CANCELLED
}