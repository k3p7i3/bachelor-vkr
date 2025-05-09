package ru.hse.fcs.order.service.interfaces.dto.converter

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import ru.hse.fcs.order.service.domain.model.OrderTariff
import ru.hse.fcs.order.service.domain.model.OrderTariffCost
import ru.hse.fcs.order.service.interfaces.dto.OrderTariffCostDto
import ru.hse.fcs.order.service.interfaces.dto.OrderTariffDto

@Component
class OrderTariffDtoToOrderTariffConverter(
    private val costConverter: Converter<OrderTariffCostDto, OrderTariffCost>
) : Converter<OrderTariffDto, OrderTariff> {
    override fun convert(source: OrderTariffDto): OrderTariff {
        return OrderTariff(
            tariffId = source.tariffId,
            applyLevel = source.applyLevel,
            isAppliedToWholeOrder = source.isAppliedToWholeOrder,
            includedOrderProductIds = source.includedOrderProductIds,
            selectedOptions = source.selectedOptions.map { convertTariffOption(it) },
            cost = source.cost?.let { costConverter.convert(it) }
        )
    }

    private fun convertTariffOption(source: OrderTariffDto.TariffOptionDto): OrderTariff.TariffOption {
        return OrderTariff.TariffOption(
            featureId = source.featureId,
            optionId = source.optionId
        )
    }
}