package ru.hse.fcs.tariff.service.interfaces.taxable.dto.converter.measurements

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import ru.hse.fcs.tariff.service.domain.measure.Volume
import ru.hse.fcs.tariff.service.domain.measure.unit.LengthUnit
import ru.hse.fcs.tariff.service.interfaces.taxable.dto.BoxVolumeDto

@Component
class BoxVolumeDtoToBoxVolumeConverter : Converter<BoxVolumeDto, Volume.BoxVolume> {
    override fun convert(source: BoxVolumeDto): Volume.BoxVolume {
        return Volume.BoxVolume(
            length = source.length,
            width = source.width,
            height = source.height,
            unit = when (source.unit) {
                BoxVolumeDto.LengthUnitDto.METRE -> LengthUnit.METRE
                BoxVolumeDto.LengthUnitDto.CENTIMETRE -> LengthUnit.CENTIMETRE
                BoxVolumeDto.LengthUnitDto.MILLIMETRE -> LengthUnit.MILLIMETRE
                BoxVolumeDto.LengthUnitDto.FOOT -> LengthUnit.FOOT
                BoxVolumeDto.LengthUnitDto.INCH -> LengthUnit.INCH
            }
        )
    }
}