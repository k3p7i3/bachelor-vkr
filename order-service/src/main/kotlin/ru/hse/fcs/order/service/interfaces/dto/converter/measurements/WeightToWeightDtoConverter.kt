package ru.hse.fcs.order.service.interfaces.dto.converter.measurements

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import ru.hse.fcs.order.service.domain.model.measurement.Weight
import ru.hse.fcs.order.service.domain.model.measurement.Weight.WeightUnit
import ru.hse.fcs.order.service.interfaces.dto.measurements.WeightDto

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