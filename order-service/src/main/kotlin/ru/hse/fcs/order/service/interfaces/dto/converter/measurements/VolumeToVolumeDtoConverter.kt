package ru.hse.fcs.order.service.interfaces.dto.converter.measurements

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import ru.hse.fcs.order.service.domain.model.measurement.Volume
import ru.hse.fcs.order.service.domain.model.measurement.Volume.VolumeUnit
import ru.hse.fcs.order.service.interfaces.dto.measurements.VolumeDto

@Component
class VolumeToVolumeDtoConverter : Converter<Volume, VolumeDto> {
    override fun convert(source: Volume): VolumeDto {
        return VolumeDto(
            value = source.value,
            unit = when (source.unit) {
                VolumeUnit.CUBIC_METER -> VolumeDto.VolumeUnitDto.CUBIC_METER
                VolumeUnit.LITER -> VolumeDto.VolumeUnitDto.LITER
                VolumeUnit.MILLILITER -> VolumeDto.VolumeUnitDto.MILLILITER
                VolumeUnit.GALLON -> VolumeDto.VolumeUnitDto.GALLON
                VolumeUnit.QUART -> VolumeDto.VolumeUnitDto.QUART
                VolumeUnit.CUBIC_FOOT -> VolumeDto.VolumeUnitDto.CUBIC_FOOT
            }
        )
    }
}