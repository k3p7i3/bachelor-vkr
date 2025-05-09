package ru.hse.fcs.tariff.service.domain.tariff

import org.springframework.data.annotation.Id
import java.util.*

data class TariffFeatureOption(
    @Id
    val optionId: UUID,
    val title: String,
    val description: String? = null
)