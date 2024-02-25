package com.ash.trading.oms.order.statemachine.handlers

import com.ash.trading.oms.order.statemachine.OmsOrderState
import com.ash.trading.oms.order.statemachine.OmsOrderStateInterface
import com.ash.trading.oms.order.statemachine.events.OmsOrderEvent
import com.ash.trading.oms.order.statemachine.events.TraderWorkingEvent

class OmsOrderNewStateHandler {

    fun <T> handleEvent(event: OmsOrderEvent<T>): OmsOrderStateInterface {
        return when (event) {
            is TraderWorkingEvent -> OmsOrderState.WORKED
        }
    }

}