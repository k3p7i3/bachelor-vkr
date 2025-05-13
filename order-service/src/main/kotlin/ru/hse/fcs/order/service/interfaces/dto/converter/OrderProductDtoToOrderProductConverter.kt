package ru.hse.fcs.order.service.interfaces.dto.converter

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import ru.hse.fcs.order.service.domain.model.OrderProduct
import ru.hse.fcs.order.service.domain.model.measurement.*
import ru.hse.fcs.order.service.interfaces.dto.OrderProductDto
import ru.hse.fcs.order.service.interfaces.dto.measurements.*

@Component
class OrderProductDtoToOrderProductConverter(
    private val priceConverter: Converter<PriceDto, Price>,
    private val weightConverter: Converter<WeightDto, Weight>,
    private val volumeConverter: Converter<VolumeDto, Volume>,
    private val boxVolumeConverter: Converter<BoxVolumeDto, BoxVolume>,
    private val densityConverter: Converter<DensityDto, Density>
    ) : Converter<OrderProductDto, OrderProduct> {
    override fun convert(source: OrderProductDto): OrderProduct {
        return OrderProduct(
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