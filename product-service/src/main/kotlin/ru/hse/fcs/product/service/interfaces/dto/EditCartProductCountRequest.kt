package ru.hse.fcs.product.service.interfaces.dto

import java.util.*

data class EditCartProductCountRequest(
    val cartProductId: UUID,
    val count: Long
)