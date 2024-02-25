package com.ash.trading.oms.trade.endpoints

import com.ash.trading.oms.model.Trade
import com.ash.trading.oms.model.TradeId
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/trade")
class TradeEndpoint {
    @GetMapping
    fun getTrades(): List<Trade> {
        TODO()
    }

    @GetMapping("/{tradeId}")
    fun getTrade(@PathVariable("tradeId") tradeId: TradeId): Trade {
        TODO()
    }

    //TODO turn this into a flux
    fun getTradeUpdates(): List<Trade> {
        TODO()
    }
}