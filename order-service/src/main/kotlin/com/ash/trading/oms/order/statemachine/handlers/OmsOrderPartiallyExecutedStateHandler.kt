package com.ash.trading.oms.order.statemachine.handlers

import com.ash.trading.oms.model.OrderQuantity
import com.ash.trading.oms.order.statemachine.OmsOrderState
import com.ash.trading.oms.order.statemachine.events.OmsOrderEvent
import com.ash.trading.oms.order.statemachine.events.OrderCancelledEvent
import com.ash.trading.oms.order.statemachine.events.TraderExecutedEvent
import org.slf4j.LoggerFactory
import java.math.BigDecimal

object OmsOrderPartiallyExecutedStateHandler {

    private val logger = LoggerFactory.getLogger(OmsOrderWorkedStateHandler::class.java)

    fun <T> handleEvent(data: OrderQuantity, event: OmsOrderEvent<T>): Pair<OrderQuantity, OmsOrderState> {
        return try {
            when (event) {
                is TraderExecutedEvent -> handleTradeExecution(data, event)
                is OrderCancelledEvent -> handleOrderCancellation(data)
                else -> handleUnplannedEvent(data, event)
            }
        } catch (e: Exception) {
            logger.error("Error Handling Event Type [${event.javaClass.simpleName}] from ${OmsOrderState.PARTIALLY_EXECUTED} state", e)
            data to OmsOrderState.PARTIALLY_EXECUTED
        }
    }

    private fun handleTradeExecution(data: OrderQuantity, event: TraderExecutedEvent): Pair<OrderQuantity, OmsOrderState> {
        val updatedData = data.copy(
            executedQuantity = event.payload.executedQuantity + data.executedQuantity,
            workedQuantity = data.workedQuantity - event.payload.executedQuantity
        )

        return if (updatedData.executedQuantity == updatedData.totalQuantity) {
            updatedData to OmsOrderState.EXECUTED
        } else {
            updatedData to OmsOrderState.PARTIALLY_EXECUTED
        }
    }

    private fun handleOrderCancellation(data: OrderQuantity): Pair<OrderQuantity, OmsOrderState> {
        val updatedData = data.copy(
            cancelledQuantity = data.workedQuantity,
            workedQuantity = BigDecimal.ZERO
        )
        return updatedData to OmsOrderState.CANCELLED
    }

    private fun <T> handleUnplannedEvent(data: OrderQuantity, event: OmsOrderEvent<T>): Pair<OrderQuantity, OmsOrderState> {
        logger.error("Invalid Event Type [${event.javaClass.simpleName}] from ${OmsOrderState.PARTIALLY_EXECUTED} state")
        return data to OmsOrderState.PARTIALLY_EXECUTED
    }

}