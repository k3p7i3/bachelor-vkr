package ru.hse.fcs.tariff.service.interfaces.tariff.dto

import java.math.BigDecimal

data class TariffNumericConditionDto(
    val minLimit: BigDecimal? = null,
    val maxLimit: BigDecimal? = null,
    val measurementType: ConditionMeasurementTypeDto,
    val measurementUnit: MeasurementUnitDto? = null
) {
    enum class ConditionMeasurementTypeDto {
        WEIGHT, VOLUME, DENSITY, UNITS, PRICE
    }
}