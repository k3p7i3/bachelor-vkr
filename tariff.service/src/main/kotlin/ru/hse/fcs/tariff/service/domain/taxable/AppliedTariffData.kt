package ru.hse.fcs.tariff.service.domain.taxable

import java.util.*

data class AppliedTariffData(
    val tariffId: UUID,
    val selectedCustomTypeOptions: Map<UUID, UUID> = emptyMap()
)