package ru.hse.fcs.order.service.interfaces.dto.converter

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import ru.hse.fcs.order.service.domain.model.OrderTariffCost
import ru.hse.fcs.order.service.domain.model.OrderTariffCost.PricePerUnit
import ru.hse.fcs.order.service.domain.model.measurement.Price
import ru.hse.fcs.order.service.interfaces.dto.OrderTariffCostDto
import ru.hse.fcs.order.service.interfaces.dto.OrderTariffCostDto.PricePerUnitDto
import ru.hse.fcs.order.service.interfaces.dto.measurements.PriceDto

@Component
class OrderTariffCostToOrderTariffCostDtoConverter(
    private val priceConverter: Converter<Price, PriceDto>
) : Converter<OrderTariffCost, OrderTariffCostDto> {
    override fun convert(source: OrderTariffCost): OrderTariffCostDto {
        return OrderTariffCostDto(
            pricePerUnit = convertPricePerUnit(source.pricePerUnit),
            unitAmount = source.unitAmount,
            cost = priceConverter.convert(source.cost)!!,
            resultCost = priceConverter.convert(source.resultCost)!!
        )
    }

    private fun convertPricePerUnit(source: PricePerUnit): PricePerUnitDto {
        return PricePerUnitDto(
            price = PricePerUnitDto.Price(
                value = source.price.value,
                unit = source.price.currency
            ),
            perUnit = convertPerUnit(source.perUnit)
        )
    }

    private fun convertPerUnit(source: PricePerUnit.PerUnit): PricePerUnitDto.PerUnitDto {
        return PricePerUnitDto.PerUnitDto(
            unitType = convertUnitType(source.unitType),
            unit = source.unit
        )
    }

    private fun convertUnitType(source: PricePerUnit.PerUnit.UnitType): PricePerUnitDto.PerUnitDto.UnitType {
        return when (source) {
            PricePerUnit.PerUnit.UnitType.PRODUCT_NUM -> PricePerUnitDto.PerUnitDto.UnitType.PRODUCT_NUM
            PricePerUnit.PerUnit.UnitType.PRODUCT_TYPE_UNIT -> PricePerUnitDto.PerUnitDto.UnitType.PRODUCT_TYPE_UNIT
            PricePerUnit.PerUnit.UnitType.WEIGHT -> PricePerUnitDto.PerUnitDto.UnitType.WEIGHT
            PricePerUnit.PerUnit.UnitType.VOLUME -> PricePerUnitDto.PerUnitDto.UnitType.VOLUME
            PricePerUnit.PerUnit.UnitType.DENSITY -> PricePerUnitDto.PerUnitDto.UnitType.DENSITY
            PricePerUnit.PerUnit.UnitType.FIXED -> PricePerUnitDto.PerUnitDto.UnitType.FIXED
            PricePerUnit.PerUnit.UnitType.PERCENTAGE -> PricePerUnitDto.PerUnitDto.UnitType.PERCENTAGE
        }
    }
}