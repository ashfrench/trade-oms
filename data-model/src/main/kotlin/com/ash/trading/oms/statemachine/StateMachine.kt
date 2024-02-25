package com.ash.trading.oms.statemachine

interface StateMachine {
    fun <U> handleState(event: Event<U>)

}