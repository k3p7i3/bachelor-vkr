package ru.hse.fcs.tariff.service.infrastructure.dto.converter

import org.bson.Document
import org.bson.types.Decimal128
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.WritingConverter
import ru.hse.fcs.tariff.service.domain.tariff.TariffNumericCondition.NumericConditionMeasurement
import ru.hse.fcs.tariff.service.domain.measure.unit.CurrencyUnit
import ru.hse.fcs.tariff.service.domain.measure.unit.DensityUnit
import ru.hse.fcs.tariff.service.domain.measure.unit.VolumeUnit
import ru.hse.fcs.tariff.service.domain.measure.unit.WeightUnit
import ru.hse.fcs.tariff.service.domain.tariff.TariffCondition
import ru.hse.fcs.tariff.service.domain.tariff.TariffNumericCondition
import ru.hse.fcs.tariff.service.domain.tariff.TariffTypeCondition

@WritingConverter
class TariffConditionWritingConverter: Converter<TariffCondition, Document> {
    override fun convert(source: TariffCondition): Document? {
        return when (source) {
            is TariffTypeCondition -> convertTypeCondition(source)
            is TariffNumericCondition -> convertNumericCondition(source)
        }
    }

    private fun convertTypeCondition(source: TariffTypeCondition): Document {
        return Document()
            .append("condition_type", "ENUM")
            .append("typeId", source.typeId)
            .append("optionId", source.optionId)
    }

    private fun convertNumericCondition(source: TariffNumericCondition): Document {
        return Document()
            .append("condition_type", "NUMERIC")
            .append("minLimit", source.minLimit?.let { Decimal128(it) })
            .append("maxLimit", source.maxLimit?.let { Decimal128(it) })
            .append("type", source.measurementUnit.type.name)
            .append(
                "unit",
                when (source.measurementUnit.type) {
                    NumericConditionMeasurement.Type.WEIGHT ->
                        (source.measurementUnit.unit as WeightUnit).name
                    NumericConditionMeasurement.Type.VOLUME ->
                        (source.measurementUnit.unit as VolumeUnit).name
                    NumericConditionMeasurement.Type.DENSITY ->
                        (source.measurementUnit.unit as DensityUnit).name
                    NumericConditionMeasurement.Type.PRICE ->
                        (source.measurementUnit.unit as CurrencyUnit).currency.name
                    NumericConditionMeasurement.Type.UNITS -> null
                }
            )
    }
}