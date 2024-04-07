package com.ash.trading.oms.model.statemachine

interface Event<T> {

    val payload: T

}