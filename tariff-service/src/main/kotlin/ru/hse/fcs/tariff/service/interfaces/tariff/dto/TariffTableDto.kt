package ru.hse.fcs.tariff.service.interfaces.tariff.dto

data class TariffTableDto(
    val commonConditions: List<TariffConditionDto>,
    val columns: List<TariffConditionDto>,
    val rows: List<TariffConditionDto>,
    val tariffPrices: List<List<TariffPriceDto?>>
)