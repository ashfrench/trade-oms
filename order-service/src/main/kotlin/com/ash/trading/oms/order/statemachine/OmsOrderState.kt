package com.ash.trading.oms.order.statemachine

import com.ash.trading.oms.model.OrderQuantity
import com.ash.trading.oms.model.compareTo
import com.ash.trading.oms.order.statemachine.events.OmsOrderEvent
import com.ash.trading.oms.order.statemachine.handlers.*
import java.math.BigDecimal

enum class OmsOrderState {


    NEW {
        override fun handleEvent(data: OrderQuantity, event: OmsOrderEvent): OrderQuantityState = OmsOrderNewStateHandler.handleEvent(data, event)
        override fun isValid(data: OrderQuantity) = data.usedQuantity == BigDecimal.ZERO

    },
    WORKED {
        override fun handleEvent(data: OrderQuantity, event: OmsOrderEvent): OrderQuantityState = OmsOrderWorkedStateHandler.handleEvent(data, event)
        override fun isValid(data: OrderQuantity) =
            data.workedQuantity > BigDecimal.ZERO && data.executedQuantity == BigDecimal.ZERO && data.cancelledQuantity.quantity == BigDecimal.ZERO
    },
    EXECUTED {
        override fun handleEvent(data: OrderQuantity, event: OmsOrderEvent): OrderQuantityState = OmsOrderExecutedStateHandler.handleEvent(data, event)
        override fun isValid(data: OrderQuantity) = data.executedQuantity == data.totalQuantity
    },
    PARTIALLY_EXECUTED {
        override fun handleEvent(data: OrderQuantity, event: OmsOrderEvent): OrderQuantityState = OmsOrderPartiallyExecutedStateHandler.handleEvent(data, event)
        override fun isValid(data: OrderQuantity) =
            data.executedQuantity > BigDecimal.ZERO && data.executedQuantity < data.totalQuantity && data.cancelledQuantity.quantity == BigDecimal.ZERO
    },
    CANCELLED {
        override fun handleEvent(data: OrderQuantity, event: OmsOrderEvent): OrderQuantityState = OmsOrderCancelledStateHandler.handleEvent(data, event)
        override fun isValid(data: OrderQuantity) =
            data.cancelledQuantity > BigDecimal.ZERO && data.openQuantity == BigDecimal.ZERO
    },
    ERROR {
        override fun handleEvent(data: OrderQuantity, event: OmsOrderEvent): OrderQuantityState {
            TODO("Not yet implemented")
        }

        override fun isValid(data: OrderQuantity) = entries.filter { it != ERROR }.none { it.isValid(data) }
    };

    abstract fun handleEvent(data: OrderQuantity, event: OmsOrderEvent): OrderQuantityState
    abstract fun isValid(data: OrderQuantity): Boolean

    fun getState(data: OrderQuantity): OmsOrderState = entries.first { it.isValid(data) }
}

operator fun OmsOrderState.contains(data: OrderQuantity) = isValid(data)

typealias OrderQuantityState = Pair<OrderQuantity, OmsOrderState>