package com.ash.trading.oms.order.statemachine

import com.ash.trading.oms.model.OrderQuantity
import com.ash.trading.oms.order.statemachine.events.OmsOrderEvent
import com.ash.trading.oms.statemachine.Event
import com.ash.trading.oms.statemachine.State
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger(IOmsOrderState::class.java)
sealed interface IOmsOrderState: State {

    fun <T> handleEvent(data: OrderQuantity, event: OmsOrderEvent<T>): Pair<OrderQuantity, IOmsOrderState>

    override fun <T> handleEvent(event: Event<T>): State {
        return if (event is OmsOrderEvent) {
            handleEvent(event)
        } else {
            logger.error("Invalid Event $event - return to original state $this")
            this
        }
    }
}

