package com.ash.trading.oms.model.statemachine

interface StateMachine {
    fun <U> handleState(event: Event<U>)

}