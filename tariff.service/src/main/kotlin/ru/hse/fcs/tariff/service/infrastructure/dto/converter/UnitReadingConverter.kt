package ru.hse.fcs.tariff.service.infrastructure.dto.converter

import org.bson.Document
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import ru.hse.fcs.tariff.service.domain.tariff.PricePerUnit
import ru.hse.fcs.tariff.service.domain.tariff.PricePerUnit.Unit.Companion.UNIT_FIELD_NAME
import ru.hse.fcs.tariff.service.domain.tariff.PricePerUnit.Unit.Companion.TYPE_FIELD_NAME
import ru.hse.fcs.tariff.service.domain.measure.unit.DensityUnit
import ru.hse.fcs.tariff.service.domain.measure.unit.VolumeUnit
import ru.hse.fcs.tariff.service.domain.measure.unit.WeightUnit

@ReadingConverter
class UnitReadingConverter : Converter<Document, PricePerUnit.Unit> {

    override fun convert(source: Document): PricePerUnit.Unit {
        val unitType = PricePerUnit.Unit.UnitType.valueOf(source.getString(TYPE_FIELD_NAME))

        val unitMeasurementName = source.getString(UNIT_FIELD_NAME)
        val unitMeasurement = when (unitType) {
            PricePerUnit.Unit.UnitType.WEIGHT ->
                WeightUnit.valueOf(unitMeasurementName)
            PricePerUnit.Unit.UnitType.VOLUME ->
                VolumeUnit.valueOf(unitMeasurementName)
            PricePerUnit.Unit.UnitType.DENSITY ->
                DensityUnit.valueOf(unitMeasurementName)
            PricePerUnit.Unit.UnitType.PRODUCT_NUM -> null
            PricePerUnit.Unit.UnitType.PRODUCT_TYPE_UNIT -> null
            PricePerUnit.Unit.UnitType.FIXED -> null
            PricePerUnit.Unit.UnitType.PERCENTAGE -> null
        }

        return PricePerUnit.Unit(
            type = unitType,
            unit = unitMeasurement
        )
    }
}