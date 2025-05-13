package ru.hse.fcs.tariff.service.interfaces.taxable.dto.converter.measurements

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import ru.hse.fcs.tariff.service.domain.measure.Density
import ru.hse.fcs.tariff.service.domain.measure.unit.DensityUnit
import ru.hse.fcs.tariff.service.interfaces.taxable.dto.DensityDto

@Component
class DensityDtoToDensityConverter : Converter<DensityDto, Density> {
    override fun convert(source: DensityDto): Density {
        return Density(
            value = source.value,
            unit = when (source.unit) {
                DensityDto.DensityUnitDto.KG_PER_CUBIC_METER -> DensityUnit.KG_PER_CUBIC_METER
                DensityDto.DensityUnitDto.GRAM_PER_CUBIC_CM -> DensityUnit.GRAM_PER_CUBIC_CM
                DensityDto.DensityUnitDto.KG_PER_CUBIC_CM -> DensityUnit.KG_PER_CUBIC_CM
                DensityDto.DensityUnitDto.GRAM_PER_CUBIC_METER -> DensityUnit.GRAM_PER_CUBIC_METER
                DensityDto.DensityUnitDto.POUNDS_PER_CUBIC_FOOT -> DensityUnit.POUNDS_PER_CUBIC_FOOT
            }
        )
    }
}