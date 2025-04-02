package ru.hse.fcs.tariff.service.interfaces.tariff.dto

data class TariffConditionDto(
    val type: TariffConditionType,
    val typeCondition: TariffTypeConditionDto? = null,
    val numericCondition: TariffNumericConditionDto? = null
) {
    enum class TariffConditionType { ENUM, NUMERIC }
}