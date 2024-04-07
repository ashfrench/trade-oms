package com.ash.trading.oms.model.request

import java.util.UUID

typealias RequestId = UUID
fun newRequestId() = UUID.randomUUID()