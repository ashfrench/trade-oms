package com.ash.trading.oms.statemachine

interface Event<T> {

    val payload: T

}