package ru.hse.fcs.order.service.interfaces.dto.converter.measurements

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import ru.hse.fcs.order.service.domain.model.measurement.BoxVolume
import ru.hse.fcs.order.service.interfaces.dto.measurements.BoxVolumeDto

@Component
class BoxVolumeDtoToBoxVolumeConverter : Converter<BoxVolumeDto, BoxVolume> {
    override fun convert(source: BoxVolumeDto): BoxVolume {
        return BoxVolume(
            length = source.length,
            width = source.width,
            height = source.height,
            unit = when (source.unit) {
                BoxVolumeDto.LengthUnitDto.METRE -> BoxVolume.LengthUnit.METRE
                BoxVolumeDto.LengthUnitDto.CENTIMETRE -> BoxVolume.LengthUnit.CENTIMETRE
                BoxVolumeDto.LengthUnitDto.MILLIMETRE -> BoxVolume.LengthUnit.MILLIMETRE
                BoxVolumeDto.LengthUnitDto.FOOT -> BoxVolume.LengthUnit.FOOT
                BoxVolumeDto.LengthUnitDto.INCH -> BoxVolume.LengthUnit.INCH
            }
        )
    }
}