package ru.hse.fcs.order.service.interfaces.dto.converter

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import ru.hse.fcs.order.service.domain.model.Order
import ru.hse.fcs.order.service.domain.model.OrderPayment
import ru.hse.fcs.order.service.domain.model.OrderProduct
import ru.hse.fcs.order.service.domain.model.OrderTariff
import ru.hse.fcs.order.service.domain.model.measurement.*
import ru.hse.fcs.order.service.interfaces.dto.OrderDto
import ru.hse.fcs.order.service.interfaces.dto.OrderPaymentDto
import ru.hse.fcs.order.service.interfaces.dto.OrderProductDto
import ru.hse.fcs.order.service.interfaces.dto.OrderTariffDto
import ru.hse.fcs.order.service.interfaces.dto.measurements.*

@Component
class OrderToOrderDtoConverter(
    private val orderProductConverter: Converter<OrderProduct, OrderProductDto>,
    private val orderTariffConverter: Converter<OrderTariff, OrderTariffDto>,
    private val orderPaymentConverter: Converter<OrderPayment, OrderPaymentDto>,
    private val priceConverter: Converter<Price, PriceDto>,
    private val weightConverter: Converter<Weight, WeightDto>,
    private val volumeConverter: Converter<Volume, VolumeDto>,
    private val boxVolumeConverter: Converter<BoxVolume, BoxVolumeDto>,
    private val densityConverter: Converter<Density, DensityDto>,
) : Converter<Order, OrderDto> {
    override fun convert(source: Order): OrderDto {
        return OrderDto(
            id = source.id,
            agentId = source.agentId,
            clientId = source.clientId,
            products = source.products.map { orderProductConverter.convert(it)!! },
            payments = source.payments.map { orderPaymentConverter.convert(it)!! },
            appliedTariffs = source.tariffs.map { orderTariffConverter.convert(it)!! },
            weight = source.weight?.let { weight ->
                OrderDto.MeasurementDto(
                    original = weightConverter.convert(weight.original)!!,
                    normalized = weight.normalized?.let { weightConverter.convert(it) }
                )
            },
            volume = source.volume?.let { volume ->
                OrderDto.MeasurementDto(
                    original = volumeConverter.convert(volume.original)!!,
                    normalized = volume.normalized?.let { volumeConverter.convert(it) }
                )
            },
            boxVolume = source.boxVolume?.let { boxVolume ->
                OrderDto.MeasurementDto(
                    original = boxVolumeConverter.convert(boxVolume.original)!!,
                    normalized = boxVolume.normalized?.let { boxVolumeConverter.convert(it) }
                )
            },
            density = source.density?.let { density ->
                OrderDto.MeasurementDto(
                    original = densityConverter.convert(density.original)!!,
                    normalized = density.normalized?.let { densityConverter.convert(it) }
                )
            },
            price = source.price?.let { price ->
                OrderDto.MeasurementDto(
                    original = priceConverter.convert(price.original)!!,
                    normalized = price.normalized?.let { priceConverter.convert(it) }
                )
            },
            totalNumber = source.totalNumber,
            totalCost = source.totalCost?.let { priceConverter.convert(it) },
            paidAmount = source.paidAmount?.let { priceConverter.convert(it) }
        )
    }
}
