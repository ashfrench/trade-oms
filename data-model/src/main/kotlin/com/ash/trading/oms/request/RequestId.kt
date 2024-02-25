package com.ash.trading.oms.request

import java.util.UUID

typealias RequestId = UUID
fun newRequestId() = UUID.randomUUID()