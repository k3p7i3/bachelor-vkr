package ru.hse.fcs.tariff.service.interfaces.taxable.dto.converter

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import ru.hse.fcs.tariff.service.domain.measure.Price
import ru.hse.fcs.tariff.service.domain.tariff.PricePerUnit
import ru.hse.fcs.tariff.service.domain.taxable.*
import ru.hse.fcs.tariff.service.interfaces.tariff.dto.TariffPriceDto
import ru.hse.fcs.tariff.service.interfaces.taxable.dto.CostDto
import ru.hse.fcs.tariff.service.interfaces.taxable.dto.OrderDto
import ru.hse.fcs.tariff.service.interfaces.taxable.dto.PriceDto
import ru.hse.fcs.tariff.service.interfaces.taxable.dto.TotalCostDto

@Component
class TotalCostToTotalCostDtoConverter(
    private val complexOrderConverter: Converter<MultiTaxableOrder, OrderDto>,
    private val orderConverter: Converter<TaxableOrder, OrderDto>,
    private val tariffPriceConverter: Converter<PricePerUnit, TariffPriceDto>,
    private val priceConverter: Converter<Price, PriceDto>
): Converter<TotalCost, TotalCostDto> {

    override fun convert(source: TotalCost): TotalCostDto {
        val complexOrder = complexOrderConverter.convert(source.complexOrder)!!

        return TotalCostDto(
            complexOrder = complexOrder,
            calculatedTariffs = source.calculatedTaxableParcels.map(::convert),
            totalCost = source.totalCost?.let { priceConverter.convert(it) }
        )
    }

    fun convert(source: TotalCost.CalculatedTaxableParcel): TotalCostDto.CalculatedTariff {
        return TotalCostDto.CalculatedTariff(
            order = orderConverter.convert(source.taxableOrder)!!,
            cost = source.cost?.let { convert(source.cost) }
        )
    }

    private fun convert(source: Cost): CostDto {
        return CostDto(
            pricePerUnit = tariffPriceConverter.convert(source.pricePerUnit)!!,
            unitAmount = source.unitAmount,
            cost = priceConverter.convert(source.cost)!!,
            resultCost = priceConverter.convert(source.resultCost)!!

        )
    }
}