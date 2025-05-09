package ru.hse.fcs.tariff.service.infrastructure.dto.converter

import org.bson.Document
import org.bson.types.Decimal128
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.WritingConverter
import ru.hse.fcs.tariff.service.domain.tariff.PricePerUnit
import ru.hse.fcs.tariff.service.domain.measure.unit.DensityUnit
import ru.hse.fcs.tariff.service.domain.measure.unit.VolumeUnit
import ru.hse.fcs.tariff.service.domain.measure.unit.WeightUnit

@WritingConverter
class PricePerUnitWritingConverter: Converter<PricePerUnit, Document> {
    override fun convert(source: PricePerUnit): Document {
        val price = Document()
            .append("value", Decimal128(source.price.value))
            .append("unit", source.price.unit.currency.name)

        val unitMeasurement = when (source.unit.type) {
            PricePerUnit.Unit.UnitType.WEIGHT ->
                (source.unit.unit as WeightUnit).name
            PricePerUnit.Unit.UnitType.VOLUME ->
                (source.unit.unit as VolumeUnit).name
            PricePerUnit.Unit.UnitType.DENSITY ->
                (source.unit.unit as DensityUnit).name
            PricePerUnit.Unit.UnitType.PRODUCT_NUM -> null
            PricePerUnit.Unit.UnitType.PRODUCT_TYPE_UNIT -> null
            PricePerUnit.Unit.UnitType.FIXED -> null
            PricePerUnit.Unit.UnitType.PERCENTAGE -> null
        }

        val unit = Document()
            .append("type", source.unit.type.name)
            .append("unit", unitMeasurement)

        return Document()
            .append("price", price)
            .append("unit", unit)
    }
}