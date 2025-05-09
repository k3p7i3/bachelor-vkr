package ru.hse.fcs.tariff.service.interfaces.taxable.dto.converter.measurements

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import ru.hse.fcs.tariff.service.domain.measure.Volume
import ru.hse.fcs.tariff.service.domain.measure.unit.LengthUnit
import ru.hse.fcs.tariff.service.interfaces.taxable.dto.BoxVolumeDto

@Component
class BoxVolumeToBoxVolumeDtoConverter : Converter<Volume.BoxVolume, BoxVolumeDto> {
    override fun convert(source: Volume.BoxVolume): BoxVolumeDto {
        return BoxVolumeDto(
            length = source.length,
            width = source.width,
            height = source.height,
            unit = when (source.unit) {
                LengthUnit.METRE -> BoxVolumeDto.LengthUnitDto.METRE
                LengthUnit.CENTIMETRE -> BoxVolumeDto.LengthUnitDto.CENTIMETRE
                LengthUnit.MILLIMETRE -> BoxVolumeDto.LengthUnitDto.MILLIMETRE
                LengthUnit.FOOT -> BoxVolumeDto.LengthUnitDto.FOOT
                LengthUnit.INCH -> BoxVolumeDto.LengthUnitDto.INCH
            }
        )
    }
}