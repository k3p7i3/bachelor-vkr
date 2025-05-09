package ru.hse.fcs.tariff.service.interfaces.taxable.dto.converter.measurements

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import ru.hse.fcs.tariff.service.domain.measure.Weight
import ru.hse.fcs.tariff.service.domain.measure.unit.WeightUnit
import ru.hse.fcs.tariff.service.interfaces.taxable.dto.WeightDto

@Component
class WeightDtoToWeightConverter: Converter<WeightDto, Weight> {
    override fun convert(source: WeightDto): Weight {
        return Weight(
            value = source.value,
            unit = when (source.unit) {
                WeightDto.WeightUnitDto.KILOGRAM -> WeightUnit.KILOGRAM
                WeightDto.WeightUnitDto.GRAM -> WeightUnit.GRAM
                WeightDto.WeightUnitDto.TON -> WeightUnit.TON
                WeightDto.WeightUnitDto.POUND -> WeightUnit.POUND
                WeightDto.WeightUnitDto.OUNCE -> WeightUnit.OUNCE
            }
        )
    }
}