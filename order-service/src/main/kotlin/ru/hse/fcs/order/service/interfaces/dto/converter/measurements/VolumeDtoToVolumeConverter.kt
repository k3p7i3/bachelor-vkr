package ru.hse.fcs.order.service.interfaces.dto.converter.measurements

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import ru.hse.fcs.order.service.domain.model.measurement.Volume
import ru.hse.fcs.order.service.domain.model.measurement.Volume.VolumeUnit
import ru.hse.fcs.order.service.interfaces.dto.measurements.VolumeDto

@Component
class VolumeDtoToVolumeConverter : Converter<VolumeDto, Volume> {
    override fun convert(source: VolumeDto): Volume {
        return Volume(
            value = source.value,
            unit = when (source.unit) {
                VolumeDto.VolumeUnitDto.CUBIC_METER -> VolumeUnit.CUBIC_METER
                VolumeDto.VolumeUnitDto.LITER -> VolumeUnit.LITER
                VolumeDto.VolumeUnitDto.MILLILITER -> VolumeUnit.MILLILITER
                VolumeDto.VolumeUnitDto.GALLON -> VolumeUnit.GALLON
                VolumeDto.VolumeUnitDto.QUART -> VolumeUnit.QUART
                VolumeDto.VolumeUnitDto.CUBIC_FOOT -> VolumeUnit.CUBIC_FOOT
            }
        )
    }
}