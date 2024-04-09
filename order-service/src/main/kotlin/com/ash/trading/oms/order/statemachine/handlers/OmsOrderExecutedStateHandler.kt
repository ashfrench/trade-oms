package com.ash.trading.oms.order.statemachine.handlers

import com.ash.trading.oms.model.OrderQuantity
import com.ash.trading.oms.order.statemachine.OmsOrderState
import com.ash.trading.oms.order.statemachine.OrderQuantityState
import com.ash.trading.oms.order.statemachine.contains
import com.ash.trading.oms.order.statemachine.events.OmsOrderEvent
import org.slf4j.LoggerFactory

object OmsOrderExecutedStateHandler {

    private val logger = LoggerFactory.getLogger(OmsOrderCancelledStateHandler::class.java)

    fun handleEvent(data: OrderQuantity, event: OmsOrderEvent): OrderQuantityState {
        check(data in OmsOrderState.EXECUTED) {
            "${OmsOrderState.EXECUTED} data should have executed quantity equal to the total quantity"
        }

        logger.warn("Invalid Event Type [${event.javaClass.simpleName}] from ${OmsOrderState.EXECUTED} state")
        return data to OmsOrderState.EXECUTED
    }

}