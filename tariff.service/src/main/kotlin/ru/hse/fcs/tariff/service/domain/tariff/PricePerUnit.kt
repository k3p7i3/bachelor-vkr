package ru.hse.fcs.tariff.service.domain.tariff

import ru.hse.fcs.tariff.service.domain.measure.Price
import ru.hse.fcs.tariff.service.domain.measure.unit.*
import ru.hse.fcs.tariff.service.domain.taxable.TaxableOrder
import ru.hse.fcs.tariff.service.domain.taxable.TaxableParcel
import ru.hse.fcs.tariff.service.domain.taxable.Cost
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

        companion object {
            const val TYPE_FIELD_NAME = "type"
            const val UNIT_FIELD_NAME = "unit"
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
                taxable.price
                    ?.toPrice(unit.unit as CurrencyUnit)
                    ?.convertTo(unit.unit)?.value
        }

        return priceMultiplier?.let {
            Cost(
                pricePerUnit = this,
                unitAmount = priceMultiplier,
                cost = Price(
                    value = priceMultiplier * price.value,
                    unit = price.unit
                )
            )
        }
    }
}