package ru.hse.fcs.tariff.service.infrastructure.dto.converter

import org.bson.Document
import org.bson.types.Decimal128
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import ru.hse.fcs.tariff.service.domain.measure.unit.Currency
import ru.hse.fcs.tariff.service.domain.tariff.TariffNumericCondition.NumericConditionMeasurement
import ru.hse.fcs.tariff.service.domain.measure.unit.*
import ru.hse.fcs.tariff.service.domain.tariff.TariffCondition
import ru.hse.fcs.tariff.service.domain.tariff.TariffNumericCondition
import ru.hse.fcs.tariff.service.domain.tariff.TariffTypeCondition
import java.util.UUID

@ReadingConverter
class TariffConditionReadingConverter: Converter<Document, TariffCondition> {
    override fun convert(source: Document): TariffCondition {
        return when (val type = source.getString("condition_type")) {
            "NUMERIC" -> convertToNumericCondition(source)
            "ENUM" -> convertToTypeCondition(source)
            else -> throw IllegalArgumentException("Unprocessable condition type = $type")
        }
    }

    private fun convertToTypeCondition(source: Document): TariffTypeCondition {
        val typeId = source.get("typeId", UUID::class.java)
        val optionId = source.get("optionId", UUID::class.java)
        return TariffTypeCondition(
            typeId = typeId,
            optionId = optionId
        )
    }

    private fun convertToNumericCondition(source: Document): TariffNumericCondition {
        val minLimit = source.get("minLimit", Decimal128::class.java)?.bigDecimalValue()
        val maxLimit = source.get("maxLimit", Decimal128::class.java)?.bigDecimalValue()

        val type = TariffNumericCondition.NumericConditionMeasurement.Type.valueOf(
            source.getString("type")
        )

        val unitName = source.getString("unit")
        val unit = when (type) {
            NumericConditionMeasurement.Type.WEIGHT ->
                WeightUnit.valueOf(unitName)
            NumericConditionMeasurement.Type.VOLUME ->
                VolumeUnit.valueOf(unitName)
            NumericConditionMeasurement.Type.DENSITY ->
                DensityUnit.valueOf(unitName)
            NumericConditionMeasurement.Type.PRICE ->
                CurrencyUnit(currency = Currency.valueOf(unitName))
            NumericConditionMeasurement.Type.UNITS -> null
        }

        return TariffNumericCondition(
            minLimit = minLimit,
            maxLimit = maxLimit,
            measurementUnit = NumericConditionMeasurement(
                type = type,
                unit = unit
            )
        )
    }
}