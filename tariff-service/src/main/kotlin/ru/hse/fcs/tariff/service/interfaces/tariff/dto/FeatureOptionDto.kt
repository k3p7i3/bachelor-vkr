package ru.hse.fcs.tariff.service.interfaces.tariff.dto

import java.util.*

data class FeatureOptionDto(
    val id: UUID? = null,
    val title: String,
    val description: String? = null
)