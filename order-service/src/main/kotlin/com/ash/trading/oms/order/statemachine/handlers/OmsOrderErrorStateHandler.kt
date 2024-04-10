package com.ash.trading.oms.order.statemachine.handlers

import com.ash.trading.oms.model.OrderQuantity
import com.ash.trading.oms.model.compareTo
import com.ash.trading.oms.order.statemachine.OmsOrderState
import com.ash.trading.oms.order.statemachine.OrderQuantityState
import com.ash.trading.oms.order.statemachine.contains
import com.ash.trading.oms.order.statemachine.events.AdminFixEvent
import com.ash.trading.oms.order.statemachine.events.OmsOrderEvent
import org.slf4j.LoggerFactory
import java.math.BigDecimal

object OmsOrderErrorStateHandler {

    private val logger = LoggerFactory.getLogger(OmsOrderErrorStateHandler::class.java)

    fun handleEvent(data: OrderQuantity, event: OmsOrderEvent): OrderQuantityState {
        check(data in OmsOrderState.ERROR) {
            val validState = OmsOrderState.entries.first { it.isValid(data) }
            "${OmsOrderState.ERROR} data should not be valid for another state $validState"
        }

        return if (event is AdminFixEvent) {
            logger.info("User ${event.userId} reset with data ${event.data} to state ${event.newState}")
            event.data to event.newState
        } else {
            logger.warn("Invalid Event Type [${event.javaClass.simpleName}] from ${OmsOrderState.ERROR} state")
            data to OmsOrderState.ERROR
        }
    }

}