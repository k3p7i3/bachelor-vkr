package ru.hse.fcs.order.service.interfaces.dto.converter

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import ru.hse.fcs.order.service.domain.model.OrderTariff
import ru.hse.fcs.order.service.domain.model.OrderTariffCost
import ru.hse.fcs.order.service.interfaces.dto.OrderTariffCostDto
import ru.hse.fcs.order.service.interfaces.dto.OrderTariffDto

@Component
class OrderTariffToOrderTariffDtoConverter(
    private val costConverter: Converter<OrderTariffCost, OrderTariffCostDto>
) : Converter<OrderTariff, OrderTariffDto> {
    override fun convert(source: OrderTariff): OrderTariffDto {
        return OrderTariffDto(
            tariffId = source.tariffId,
            applyLevel = source.applyLevel,
            isAppliedToWholeOrder = source.isAppliedToWholeOrder,
            includedOrderProductIds = source.includedOrderProductIds,
            selectedOptions = source.selectedOptions.map { convertTariffOption(it) },
            cost = source.cost?.let { costConverter.convert(it) }
        )
    }

    private fun convertTariffOption(source: OrderTariff.TariffOption): OrderTariffDto.TariffOptionDto {
        return OrderTariffDto.TariffOptionDto(
            featureId = source.featureId,
            optionId = source.optionId
        )
    }
}