package com.ash.trading.oms.order.statemachine.handlers

import com.ash.trading.oms.model.OrderQuantity
import com.ash.trading.oms.order.statemachine.OmsOrderState
import com.ash.trading.oms.order.statemachine.OrderQuantityState
import com.ash.trading.oms.order.statemachine.contains
import com.ash.trading.oms.order.statemachine.events.OmsOrderEvent
import org.slf4j.LoggerFactory

object OmsOrderCancelledStateHandler: OmsOrderStateHandler {

    private val logger = LoggerFactory.getLogger(OmsOrderCancelledStateHandler::class.java)

    override fun handleEvent(data: OrderQuantity, event: OmsOrderEvent): OrderQuantityState {
        check(data in OmsOrderState.CANCELLED) {
            "${OmsOrderState.CANCELLED} data should have some cancelled quantity"
        }
        logger.warn("Invalid Event Type [${event.javaClass.simpleName}] from ${OmsOrderState.CANCELLED} state")
        return data to OmsOrderState.CANCELLED
    }

}