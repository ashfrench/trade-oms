package com.ash.trading.oms.model.statemachine

interface State {

    fun <T> handleEvent(event: Event<T>): State

}