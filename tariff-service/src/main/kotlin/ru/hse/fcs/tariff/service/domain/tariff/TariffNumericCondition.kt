package ru.hse.fcs.tariff.service.domain.tariff

import org.springframework.data.annotation.TypeAlias
import ru.hse.fcs.tariff.service.domain.measure.*
import ru.hse.fcs.tariff.service.domain.measure.unit.*
import ru.hse.fcs.tariff.service.domain.taxable.TaxableParcel
import java.math.BigDecimal

@TypeAlias("numeric")
data class TariffNumericCondition(
    val measurementUnit: NumericConditionMeasurement,
    val minLimit: BigDecimal? = null,
    val maxLimit: BigDecimal? = null,
) : TariffCondition {

    class NumericConditionMeasurement(
        val type: Type,
        val unit: LinearConvertible<*>? = null
    ) {
        enum class Type {
            WEIGHT, VOLUME, DENSITY, UNITS, PRICE
        }

        companion object {
            const val TYPE_FIELD_NAME = "type"
            const val UNIT_FIELD_NAME = "unit"
        }

        override fun equals(other: Any?): Boolean {
            if (other is NumericConditionMeasurement) {
                return this.type == other.type && this.unit == other.unit
            }
            return super.equals(other)
        }
    }

    override fun isMatching(taxable: TaxableParcel): Boolean {
        return when (measurementUnit.type) {
            NumericConditionMeasurement.Type.WEIGHT ->
                isMatching<WeightUnit>(taxable.weight)
            NumericConditionMeasurement.Type.VOLUME ->
                isMatching<VolumeUnit>(taxable.volume)
            NumericConditionMeasurement.Type.DENSITY ->
                isMatching<DensityUnit>(taxable.density)
            NumericConditionMeasurement.Type.PRICE ->
                isMatching<CurrencyUnit>(taxable.price)
            NumericConditionMeasurement.Type.UNITS ->
                taxable.totalNumber?.let { totalNumber ->
                    isMatching(totalNumber.toBigDecimal())
                } ?: false
        }
    }

    private inline fun <reified T : LinearConvertible<T>> isMatching(property: ConvertibleMeasurement<T>?): Boolean {
        return property?.let {
            if (measurementUnit.unit is T) {
                val rate = property.unit.getConversionRateTo(measurementUnit.unit)
                isMatching(property.value.multiply(rate))
            } else {
                throw IllegalStateException("Wrong property type was passed")
            }
        } ?: false
    }

    private fun isMatching(convertedProperty: BigDecimal): Boolean {
        return minLimit?.let {
            minLimit <= convertedProperty
        } != false &&
                maxLimit?.let {
                    maxLimit > convertedProperty
                } != false
    }


    override fun isOverlapping(other: TariffCondition): Boolean =
        when (other) {
            is TariffTypeCondition -> false
            is TariffNumericCondition -> isOverlapping(other)
        }

    private fun isOverlapping(other: TariffNumericCondition): Boolean {
        if (measurementUnit.type != other.measurementUnit.type) {
            return false
        }

        val rate = when (other.measurementUnit.unit) {
            is WeightUnit -> getRateTo(other.measurementUnit.unit)
            is VolumeUnit -> getRateTo(other.measurementUnit.unit)
            is DensityUnit -> getRateTo(other.measurementUnit.unit)
            is CurrencyUnit -> getRateTo(other.measurementUnit.unit)
            else -> BigDecimal.ONE
        }

        val normalisedMinLimit = minLimit?.multiply(rate)
        val normalisedMaxLimit = maxLimit?.multiply(rate)

        val thisIsLess = isFirstLessThenSecond(normalisedMaxLimit, other.minLimit)
        val otherIsLess = isFirstLessThenSecond(other.maxLimit, normalisedMinLimit)

        return thisIsLess.not() && otherIsLess.not()
    }

    private fun isFirstLessThenSecond(firstMax: BigDecimal?, secondMin: BigDecimal?) =
        firstMax?.let {
            secondMin?.let {
                firstMax <= secondMin
            }
        } ?: false

    private inline fun <reified T : LinearConvertible<T>> getRateTo(other: T): BigDecimal {
        if (measurementUnit.unit is T) {
            return measurementUnit.unit.getConversionRateTo(other)
        } else {
            throw IllegalStateException("Wrong unit type was passed")
        }
    }
}


