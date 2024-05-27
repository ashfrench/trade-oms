package com.ash.trading.oms.order.statemachine.handlers

import com.ash.trading.oms.model.OrderQuantity
import com.ash.trading.oms.order.statemachine.OmsOrderState
import com.ash.trading.oms.order.statemachine.OrderQuantityState
import com.ash.trading.oms.order.statemachine.events.AdminFixEvent
import com.ash.trading.oms.order.statemachine.events.OmsOrderEvent
import org.slf4j.LoggerFactory

internal data object OmsOrderErrorStateHandler: OmsOrderStateHandler {

    private val logger = LoggerFactory.getLogger(OmsOrderErrorStateHandler::class.java)

    override fun handleEvent(data: OrderQuantity, event: OmsOrderEvent): OrderQuantityState {
        return try {
            when (event) {
                is AdminFixEvent -> handleAdminFixEvent(data, event)
                else -> handleUnplannedEvent(data, event)
            }
        } catch (e: Exception) {
            logger.error("Error Handling Event Type [${event.javaClass.simpleName}] from ${OmsOrderState.ERROR} state", e)
            data to OmsOrderState.ERROR
        }
    }

    private fun handleAdminFixEvent(
        data: OrderQuantity,
        event: AdminFixEvent
    ): OrderQuantityState {
        val (userId, newData, newState) = event

        logger.info("User $userId reset with data $newData to state $newState")
        return if (newState.isValid(newData)) {
            newData to newState
        } else {
            logger.warn("Updated Data ${event.data} was invalid for new state ${event.newState}")
            data to OmsOrderState.ERROR
        }
    }

    private fun handleUnplannedEvent(data: OrderQuantity, event: OmsOrderEvent): OrderQuantityState {
        logger.warn("Invalid Event Type [${event.javaClass.simpleName}] from ${OmsOrderState.ERROR} state")
        return data to OmsOrderState.ERROR
    }

}