package ru.hse.fcs.order.service.interfaces.dto.converter.measurements

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import ru.hse.fcs.order.service.domain.model.measurement.Density
import ru.hse.fcs.order.service.domain.model.measurement.Density.DensityUnit
import ru.hse.fcs.order.service.interfaces.dto.measurements.DensityDto

@Component
class DensityToDensityDtoConverter : Converter<Density, DensityDto> {
    override fun convert(source: Density): DensityDto {
        return DensityDto(
            value = source.value,
            unit = when (source.unit) {
                DensityUnit.KG_PER_CUBIC_METER -> DensityDto.DensityUnitDto.KG_PER_CUBIC_METER
                DensityUnit.GRAM_PER_CUBIC_CM -> DensityDto.DensityUnitDto.GRAM_PER_CUBIC_CM
                DensityUnit.KG_PER_CUBIC_CM -> DensityDto.DensityUnitDto.KG_PER_CUBIC_CM
                DensityUnit.GRAM_PER_CUBIC_METER -> DensityDto.DensityUnitDto.GRAM_PER_CUBIC_METER
                DensityUnit.POUNDS_PER_CUBIC_FOOT -> DensityDto.DensityUnitDto.POUNDS_PER_CUBIC_FOOT
            }
        )
    }
}