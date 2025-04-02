package ru.hse.fcs.tariff.service.infrastructure.dto.converter

import org.bson.Document
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.WritingConverter
import ru.hse.fcs.tariff.service.domain.tariff.PricePerUnit
import ru.hse.fcs.tariff.service.domain.tariff.PricePerUnit.Unit.Companion.UNIT_FIELD_NAME
import ru.hse.fcs.tariff.service.domain.tariff.PricePerUnit.Unit.Companion.TYPE_FIELD_NAME
import ru.hse.fcs.tariff.service.domain.measure.unit.DensityUnit
import ru.hse.fcs.tariff.service.domain.measure.unit.VolumeUnit
import ru.hse.fcs.tariff.service.domain.measure.unit.WeightUnit

@WritingConverter
class UnitWritingConverter: Converter<PricePerUnit.Unit, Document> {

    override fun convert(source: PricePerUnit.Unit): Document =
       Document(
            mapOf(
                TYPE_FIELD_NAME to source.type.name,
                UNIT_FIELD_NAME to
                    when (source.type) {
                        PricePerUnit.Unit.UnitType.WEIGHT ->
                            (source.unit as WeightUnit).name
                        PricePerUnit.Unit.UnitType.VOLUME ->
                            (source.unit as VolumeUnit).name
                        PricePerUnit.Unit.UnitType.DENSITY ->
                            (source.unit as DensityUnit).name
                        PricePerUnit.Unit.UnitType.PRODUCT_NUM -> null
                        PricePerUnit.Unit.UnitType.PRODUCT_TYPE_UNIT -> null
                        PricePerUnit.Unit.UnitType.FIXED -> null
                        PricePerUnit.Unit.UnitType.PERCENTAGE -> null
                    }
            )
       )
}