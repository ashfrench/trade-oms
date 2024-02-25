package com.ash.trading.oms.tradeorder.endpoints

import com.ash.trading.oms.model.TradeOrder
import com.ash.trading.oms.model.TradeOrderId
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/trade-order")
class TradeOrderEndpoint {
    @GetMapping
    fun getTradeOrders(): List<TradeOrder> {
        TODO()
    }

    @GetMapping("/{tradeOrderId}")
    fun getTradeOrder(@PathVariable("tradeOrderId") tradeOrderId: TradeOrderId): TradeOrder {
        TODO()
    }

    //TODO turn this into a flux
    fun getTradeOrderUpdates(): List<TradeOrder> {
        TODO()
    }
}