package ru.hse.fcs.tariff.service.domain.tariff

import ru.hse.fcs.tariff.service.domain.measure.Price
import ru.hse.fcs.tariff.service.domain.measure.unit.*
import ru.hse.fcs.tariff.service.domain.taxable.TaxableOrder
import ru.hse.fcs.tariff.service.domain.taxable.TaxableParcel
import ru.hse.fcs.tariff.service.domain.taxable.Cost
import ru.hse.fcs.tariff.service.domain.taxable.exception.NotEnoughOrderDataException
import java.math.BigDecimal

data class PricePerUnit(
    val price: Price,
    val unit: Unit
) {

    data class Unit(
        val type: UnitType,
        val unit: LinearConvertible<*>? = null
    ) {
        enum class UnitType {
            PRODUCT_NUM, PRODUCT_TYPE_UNIT, WEIGHT, VOLUME, DENSITY, FIXED, PERCENTAGE
        }
    }

    fun calculateCost(taxable: TaxableParcel): Cost? {
        val priceMultiplier = when (unit.type) {
            Unit.UnitType.PRODUCT_NUM ->
                taxable.totalNumber?.toBigDecimal()
            Unit.UnitType.PRODUCT_TYPE_UNIT ->
                when (taxable) {
                    is TaxableOrder -> taxable.products.size.toBigDecimal()
                    else -> BigDecimal.ONE
                }
            Unit.UnitType.WEIGHT ->
                taxable.weight?.convertTo(unit.unit as WeightUnit)?.value
            Unit.UnitType.VOLUME ->
                taxable.volume?.convertTo(unit.unit as VolumeUnit)?.value
            Unit.UnitType.DENSITY ->
                taxable.density?.convertTo(unit.unit as DensityUnit)?.value
            Unit.UnitType.FIXED -> BigDecimal.ONE
            Unit.UnitType.PERCENTAGE ->
                taxable.price?.convertTo(price.unit)?.value
        }

        if (priceMultiplier == null) {
            throw NotEnoughOrderDataException()
        }

        val cost = Price(
            value = priceMultiplier * price.value,
            unit = price.unit
        )

        return Cost(
            pricePerUnit = this,
            unitAmount = priceMultiplier,
            cost = cost,
            resultCost = cost.convertTo(
                CurrencyUnit(
                    currency = taxable.appliedTariff.finalCurrency,
                    exchangeRates = cost.unit.exchangeRates
                )
            ) as Price
        )
    }
}