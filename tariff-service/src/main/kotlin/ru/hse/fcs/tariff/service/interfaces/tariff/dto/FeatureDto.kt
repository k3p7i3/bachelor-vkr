package ru.hse.fcs.tariff.service.interfaces.tariff.dto

import java.util.*

data class FeatureDto(
    val id: UUID? = null,
    val title: String,
    val description: String? = null,
    val options: List<FeatureOptionDto> = emptyList()
)