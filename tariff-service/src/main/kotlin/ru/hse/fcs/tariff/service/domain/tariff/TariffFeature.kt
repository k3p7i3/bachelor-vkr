package ru.hse.fcs.tariff.service.domain.tariff

import org.springframework.data.annotation.Id
import java.util.*

data class TariffFeature(
    @Id
    val featureId: UUID = UUID.randomUUID(),
    val title: String,
    val description: String? = null,
    val options: List<TariffFeatureOption> = emptyList()
)

