package ru.hse.fcs.tariff.service.infrastructure.dto.converter

import org.bson.Document
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.WritingConverter
import ru.hse.fcs.tariff.service.domain.tariff.TariffNumericCondition.NumericConditionMeasurement
import ru.hse.fcs.tariff.service.domain.tariff.TariffNumericCondition.NumericConditionMeasurement.Companion.TYPE_FIELD_NAME
import ru.hse.fcs.tariff.service.domain.tariff.TariffNumericCondition.NumericConditionMeasurement.Companion.UNIT_FIELD_NAME
import ru.hse.fcs.tariff.service.domain.measure.unit.CurrencyUnit
import ru.hse.fcs.tariff.service.domain.measure.unit.DensityUnit
import ru.hse.fcs.tariff.service.domain.measure.unit.VolumeUnit
import ru.hse.fcs.tariff.service.domain.measure.unit.WeightUnit

@WritingConverter
class NumericConditionMeasurementWritingConverter: Converter<NumericConditionMeasurement, Document> {

    override fun convert(source: NumericConditionMeasurement): Document =
        Document(
            mapOf(
                TYPE_FIELD_NAME to source.type.name,
                UNIT_FIELD_NAME to
                    when (source.type) {
                        NumericConditionMeasurement.Type.WEIGHT ->
                            (source.unit as WeightUnit).name
                        NumericConditionMeasurement.Type.VOLUME ->
                            (source.unit as VolumeUnit).name
                        NumericConditionMeasurement.Type.DENSITY ->
                            (source.unit as DensityUnit).name
                        NumericConditionMeasurement.Type.PRICE ->
                            (source.unit as CurrencyUnit).currency.name
                        NumericConditionMeasurement.Type.UNITS -> null
                    }
            )
        )
}