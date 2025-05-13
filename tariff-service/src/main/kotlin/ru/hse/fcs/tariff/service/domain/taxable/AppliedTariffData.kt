package ru.hse.fcs.tariff.service.domain.taxable

import ru.hse.fcs.tariff.service.domain.measure.unit.Currency
import java.util.*

data class AppliedTariffData(
    val tariffId: UUID,
    val selectedCustomTypeOptions: Map<UUID, UUID> = emptyMap(),
    val finalCurrency: Currency
)