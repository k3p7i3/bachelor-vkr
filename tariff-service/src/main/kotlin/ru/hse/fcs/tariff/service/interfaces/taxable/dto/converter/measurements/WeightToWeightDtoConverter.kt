package ru.hse.fcs.tariff.service.interfaces.taxable.dto.converter.measurements

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import ru.hse.fcs.tariff.service.domain.measure.Weight
import ru.hse.fcs.tariff.service.domain.measure.unit.WeightUnit
import ru.hse.fcs.tariff.service.interfaces.taxable.dto.WeightDto

@Component
class WeightToWeightDtoConverter: Converter<Weight, WeightDto> {
    override fun convert(source: Weight): WeightDto {
        return WeightDto(
            value = source.value,
            unit = when (source.unit) {
                WeightUnit.KILOGRAM -> WeightDto.WeightUnitDto.KILOGRAM
                WeightUnit.GRAM -> WeightDto.WeightUnitDto.GRAM
                WeightUnit.TON -> WeightDto.WeightUnitDto.TON
                WeightUnit.POUND -> WeightDto.WeightUnitDto.POUND
                WeightUnit.OUNCE -> WeightDto.WeightUnitDto.OUNCE
            }
        )
    }
}