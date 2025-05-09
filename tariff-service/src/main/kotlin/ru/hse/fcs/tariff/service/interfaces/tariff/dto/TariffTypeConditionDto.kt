package ru.hse.fcs.tariff.service.interfaces.tariff.dto

import java.util.UUID

data class TariffTypeConditionDto(
    val feature: UUID,
    val option: UUID
)