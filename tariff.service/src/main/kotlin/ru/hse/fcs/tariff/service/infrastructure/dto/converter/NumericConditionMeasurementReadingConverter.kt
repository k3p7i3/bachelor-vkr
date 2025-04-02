package ru.hse.fcs.tariff.service.infrastructure.dto.converter

import org.bson.Document
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import ru.hse.fcs.tariff.service.domain.measure.unit.Currency
import ru.hse.fcs.tariff.service.domain.tariff.TariffNumericCondition.NumericConditionMeasurement
import ru.hse.fcs.tariff.service.domain.tariff.TariffNumericCondition.NumericConditionMeasurement.Companion.TYPE_FIELD_NAME
import ru.hse.fcs.tariff.service.domain.tariff.TariffNumericCondition.NumericConditionMeasurement.Companion.UNIT_FIELD_NAME
import ru.hse.fcs.tariff.service.domain.measure.unit.*
import java.util.*

@ReadingConverter
class NumericConditionMeasurementReadingConverter(
    private val currencyUnitFactory: CurrencyUnitFactory
): Converter<Document, NumericConditionMeasurement> {

    override fun convert(source: Document): NumericConditionMeasurement {
        val type = NumericConditionMeasurement.Type.valueOf(
            source.getString(TYPE_FIELD_NAME)
        )

        val unitName = source.getString(UNIT_FIELD_NAME)
        val unit = when (type) {
            NumericConditionMeasurement.Type.WEIGHT ->
                WeightUnit.valueOf(unitName)
            NumericConditionMeasurement.Type.VOLUME ->
                VolumeUnit.valueOf(unitName)
            NumericConditionMeasurement.Type.DENSITY ->
                DensityUnit.valueOf(unitName)
            NumericConditionMeasurement.Type.PRICE ->
                currencyUnitFactory.buildCurrencyUnit(
                    agentId = UUID.fromString(source.getString("agent_id")), // TODO test it, because it shouldn't work?
                    currency = Currency.valueOf(unitName)
                )
            NumericConditionMeasurement.Type.UNITS -> null
        }

        return NumericConditionMeasurement(
            type = type,
            unit = unit
        )
    }
}