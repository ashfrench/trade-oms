package com.ash.trading.oms.order.statemachine

import com.ash.trading.oms.model.OrderQuantity
import com.ash.trading.oms.model.compareTo
import com.ash.trading.oms.order.statemachine.events.OmsOrderEvent
import com.ash.trading.oms.order.statemachine.handlers.*
import java.math.BigDecimal

enum class OmsOrderState {


    NEW {
        override fun isValid(data: OrderQuantity) = data.usedQuantity == BigDecimal.ZERO
        override fun validate(data: OrderQuantity) {
            check (data in NEW) {
                "$NEW Data should have ZERO used quantity"
            }
        }

        override val handler: OmsOrderStateHandler = OmsOrderNewStateHandler
    },
    WORKED {
        override fun isValid(data: OrderQuantity) = data.workedQuantity > BigDecimal.ZERO && data.executedQuantity == BigDecimal.ZERO && data.cancelledQuantity.quantity == BigDecimal.ZERO
        override fun validate(data: OrderQuantity) {
            check (data in WORKED) {
                "$WORKED Data should have worked quantity greater than ZERO and open quantity greater than ZERO"
            }
        }

        override val handler: OmsOrderStateHandler = OmsOrderWorkedStateHandler
    },
    EXECUTED {
        override fun isValid(data: OrderQuantity) = data.executedQuantity == data.totalQuantity
        override fun validate(data: OrderQuantity) {
            check(data in EXECUTED) {
                "$EXECUTED data should have executed quantity equal to the total quantity"
            }
        }

        override val handler: OmsOrderStateHandler = OmsOrderExecutedStateHandler
    },
    PARTIALLY_EXECUTED {
        override fun isValid(data: OrderQuantity) = data.executedQuantity > BigDecimal.ZERO && data.executedQuantity < data.totalQuantity && data.cancelledQuantity.quantity == BigDecimal.ZERO
        override fun validate(data: OrderQuantity) {
            check (data in PARTIALLY_EXECUTED) {
                "$PARTIALLY_EXECUTED Data should have executed quantity greater than ZERO and less than the total quantity"
            }
        }

        override val handler: OmsOrderStateHandler = OmsOrderPartiallyExecutedStateHandler
    },
    CANCELLED {
        override fun isValid(data: OrderQuantity) = data.cancelledQuantity > BigDecimal.ZERO && data.openQuantity == BigDecimal.ZERO
        override fun validate(data: OrderQuantity) {
            check(data in CANCELLED) {
                "$CANCELLED data should have some cancelled quantity"
            }
        }

        override val handler: OmsOrderStateHandler = OmsOrderCancelledStateHandler
    },
    ERROR {
        override fun isValid(data: OrderQuantity) = entries.filter { it != ERROR }.none { it.isValid(data) }
        override fun validate(data: OrderQuantity) {
            check(data in ERROR) {
                val validState = OmsOrderState.entries.first { it.isValid(data) }
                "$ERROR data should not be valid for another state $validState"
            }
        }

        override val handler: OmsOrderStateHandler = OmsOrderErrorStateHandler
    };

    fun handleEvent(data: OrderQuantity, event: OmsOrderEvent): OrderQuantityState {
        validate(data)
        return handler.handleEvent(data, event).also {
            validate(it)
        }
    }

    abstract fun isValid(data: OrderQuantity): Boolean
    abstract fun validate(data: OrderQuantity)
    abstract val handler: OmsOrderStateHandler

    fun getState(data: OrderQuantity): OmsOrderState = entries.first { it.isValid(data) }
}

operator fun OmsOrderState.contains(data: OrderQuantity) = isValid(data)

typealias OrderQuantityState = Pair<OrderQuantity, OmsOrderState>
private fun validate(stateQuantity: OrderQuantityState) = stateQuantity.second.validate(stateQuantity.first)