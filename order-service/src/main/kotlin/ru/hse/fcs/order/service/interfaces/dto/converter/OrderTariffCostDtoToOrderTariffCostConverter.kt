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
class OrderTariffCostDtoToOrderTariffCostConverter(
    private val priceConverter: Converter<PriceDto, Price>
) : Converter<OrderTariffCostDto, OrderTariffCost> {
    override fun convert(source: OrderTariffCostDto): OrderTariffCost {
        return OrderTariffCost(
            pricePerUnit = convertPricePerUnit(source.pricePerUnit),
            unitAmount = source.unitAmount,
            cost = priceConverter.convert(source.cost)!!,
            resultCost = priceConverter.convert(source.resultCost)!!
        )
    }

    private fun convertPricePerUnit(source: PricePerUnitDto): PricePerUnit {
        return PricePerUnit(
            price = Price(
                value = source.price.value,
                currency = source.price.unit
            ),
            perUnit = convertPerUnit(source.perUnit)
        )
    }

    private fun convertPerUnit(source: PricePerUnitDto.PerUnitDto): PricePerUnit.PerUnit {
        return PricePerUnit.PerUnit(
            unitType = convertUnitType(source.unitType),
            unit = source.unit
        )
    }

    private fun convertUnitType(source: PricePerUnitDto.PerUnitDto.UnitType): PricePerUnit.PerUnit.UnitType {
        return when (source) {
            PricePerUnitDto.PerUnitDto.UnitType.PRODUCT_NUM -> PricePerUnit.PerUnit.UnitType.PRODUCT_NUM
            PricePerUnitDto.PerUnitDto.UnitType.PRODUCT_TYPE_UNIT -> PricePerUnit.PerUnit.UnitType.PRODUCT_TYPE_UNIT
            PricePerUnitDto.PerUnitDto.UnitType.WEIGHT -> PricePerUnit.PerUnit.UnitType.WEIGHT
            PricePerUnitDto.PerUnitDto.UnitType.VOLUME -> PricePerUnit.PerUnit.UnitType.VOLUME
            PricePerUnitDto.PerUnitDto.UnitType.DENSITY -> PricePerUnit.PerUnit.UnitType.DENSITY
            PricePerUnitDto.PerUnitDto.UnitType.FIXED -> PricePerUnit.PerUnit.UnitType.FIXED
            PricePerUnitDto.PerUnitDto.UnitType.PERCENTAGE -> PricePerUnit.PerUnit.UnitType.PERCENTAGE
        }
    }
}