package ru.hse.fcs.tariff.service.interfaces.taxable.dto.converter.measurements

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import ru.hse.fcs.tariff.service.domain.measure.Volume
import ru.hse.fcs.tariff.service.domain.measure.unit.VolumeUnit
import ru.hse.fcs.tariff.service.interfaces.taxable.dto.VolumeDto

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