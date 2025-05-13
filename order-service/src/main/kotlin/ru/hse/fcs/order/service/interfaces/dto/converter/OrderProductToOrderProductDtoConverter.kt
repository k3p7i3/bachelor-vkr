package ru.hse.fcs.order.service.interfaces.dto.converter

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import ru.hse.fcs.order.service.domain.model.OrderProduct
import ru.hse.fcs.order.service.domain.model.measurement.*
import ru.hse.fcs.order.service.interfaces.dto.OrderProductDto
import ru.hse.fcs.order.service.interfaces.dto.measurements.*

@Component
class OrderProductToOrderProductDtoConverter(
    private val priceConverter: Converter<Price, PriceDto>,
    private val weightConverter: Converter<Weight, WeightDto>,
    private val volumeConverter: Converter<Volume, VolumeDto>,
    private val boxVolumeConverter: Converter<BoxVolume, BoxVolumeDto>,
    private val densityConverter: Converter<Density, DensityDto>,
) : Converter<OrderProduct, OrderProductDto> {
    override fun convert(source: OrderProduct): OrderProductDto {
        return OrderProductDto(
            productId = source.productId,
            totalNumber = source.totalNumber,
            price = source.price?.let { priceConverter.convert(it) },
            weight = source.weight?.let { weightConverter.convert(it) },
            volume = source.volume?.let { volumeConverter.convert(it) },
            boxVolume = source.boxVolume?.let { boxVolumeConverter.convert(it) },
            density = source.density?.let { densityConverter.convert(it) }
        )
    }
}