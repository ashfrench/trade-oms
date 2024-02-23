package com.ash.trading.oms.order.endpoints

import com.ash.trading.oms.model.Order
import com.ash.trading.oms.model.OrderId
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/order")
class OrderEndpoint {


    @GetMapping
    fun getOrders(): List<Order> {
        TODO()
    }

    @GetMapping("/{orderId}")
    fun getOrder(orderId: OrderId): Order {
        TODO()
    }

}