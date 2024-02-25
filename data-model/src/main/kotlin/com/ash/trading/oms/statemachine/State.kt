package com.ash.trading.oms.statemachine

interface State {

    fun <T> handleEvent(event: Event<T>): State

}