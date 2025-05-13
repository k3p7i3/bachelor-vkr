package ru.hse.fcs.order.service.interfaces.dto.converter

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import ru.hse.fcs.order.service.domain.model.OrderPayment
import ru.hse.fcs.order.service.domain.model.measurement.Price
import ru.hse.fcs.order.service.interfaces.dto.OrderPaymentDto
import ru.hse.fcs.order.service.interfaces.dto.measurements.PriceDto

@Component
class OrderPaymentToOrderPaymentDtoConverter(
    private val priceConverter: Converter<Price, PriceDto>
) : Converter<OrderPayment, OrderPaymentDto> {
    override fun convert(source: OrderPayment): OrderPaymentDto {
        return OrderPaymentDto(
            id = source.id,
            externalId = source.externalId,
            confirmationToken = source.confirmationToken,
            status = source.status,
            description = source.description,
            includedTariffs = source.includedTariffs.map { convertTariffPayment(it) },
            amount = priceConverter.convert(source.amount)!!,
            createdAt = source.createdAt
        )
    }

    private fun convertTariffPayment(source: OrderPayment.TariffPayment) =
        OrderPaymentDto.TariffPaymentDto(
            tariffId = source.tariffId,
            amount = source.amount
        )
}