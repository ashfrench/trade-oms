package com.ash.trading.oms.model.user

import java.util.*

data class User(
    val userId: UserId,
    val userName: String
)

typealias UserId = UUID