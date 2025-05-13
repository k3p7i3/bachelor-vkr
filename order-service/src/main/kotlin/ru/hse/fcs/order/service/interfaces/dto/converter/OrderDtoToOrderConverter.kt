package ru.hse.fcs.order.service.interfaces.dto.converter

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import ru.hse.fcs.order.service.domain.model.Order
import ru.hse.fcs.order.service.domain.model.OrderPayment
import ru.hse.fcs.order.service.domain.model.measurement.*
import ru.hse.fcs.order.service.interfaces.dto.OrderDto
import ru.hse.fcs.order.service.interfaces.dto.OrderPaymentDto
import ru.hse.fcs.order.service.interfaces.dto.measurements.*
import java.util.*

@Component
class OrderDtoToOrderConverter(
    private val orderProductConverter: OrderProductDtoToOrderProductConverter,
    private val orderTariffConverter: OrderTariffDtoToOrderTariffConverter,
    private val orderPaymentConverter: Converter<OrderPaymentDto, OrderPayment>,
    private val priceConverter: Converter<PriceDto, Price>,
    private val weightConverter: Converter<WeightDto, Weight>,
    private val volumeConverter: Converter<VolumeDto, Volume>,
    private val boxVolumeConverter: Converter<BoxVolumeDto, BoxVolume>,
    private val densityConverter: Converter<DensityDto, Density>
) : Converter<OrderDto, Order> {
    override fun convert(source: OrderDto): Order {
        return Order(
            id = source.id ?: UUID.randomUUID(),
            agentId = source.agentId,
            clientId = source.clientId,
            products = source.products.map { orderProductConverter.convert(it) },
            tariffs = source.appliedTariffs.map { orderTariffConverter.convert(it) },
            payments = source.payments.map { orderPaymentConverter.convert(it)!! },
            weight = source.weight?.let { weight ->
                Measurement(
                    original = weightConverter.convert(weight.original)!!,
                    normalized = weight.normalized?.let { weightConverter.convert(it) }
                )
            },
            volume = source.volume?.let { volume ->
                Measurement(
                    original = volumeConverter.convert(volume.original)!!,
                    normalized = volume.normalized?.let { volumeConverter.convert(it) }
                )
            },
            boxVolume = source.boxVolume?.let { boxVolume ->
                Measurement(
                    original = boxVolumeConverter.convert(boxVolume.original)!!,
                    normalized = boxVolume.normalized?.let { boxVolumeConverter.convert(it) }
                )
            },
            density = source.density?.let { density ->
                Measurement(
                    original = densityConverter.convert(density.original)!!,
                    normalized = density.normalized?.let { densityConverter.convert(it) }
                )
            },
            price = source.price?.let { price ->
                Measurement(
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