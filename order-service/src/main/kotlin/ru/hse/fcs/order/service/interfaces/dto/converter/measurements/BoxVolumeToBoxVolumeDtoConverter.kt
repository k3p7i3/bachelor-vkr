package ru.hse.fcs.order.service.interfaces.dto.converter.measurements

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import ru.hse.fcs.order.service.domain.model.measurement.BoxVolume
import ru.hse.fcs.order.service.domain.model.measurement.BoxVolume.LengthUnit
import ru.hse.fcs.order.service.interfaces.dto.measurements.BoxVolumeDto

@Component
class BoxVolumeToBoxVolumeDtoConverter : Converter<BoxVolume, BoxVolumeDto> {
    override fun convert(source: BoxVolume): BoxVolumeDto {
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