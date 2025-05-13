package ru.hse.fcs.tariff.service.infrastructure.dto.converter

import org.bson.Document
import org.bson.types.Decimal128
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import ru.hse.fcs.tariff.service.domain.measure.Price
import ru.hse.fcs.tariff.service.domain.measure.unit.*
import ru.hse.fcs.tariff.service.domain.tariff.PricePerUnit

@ReadingConverter
class PricePerUnitReadingConverter : Converter<Document, PricePerUnit> {

    override fun convert(source: Document): PricePerUnit {
        val price = source.get("price", Document::class.java)
        val priceValue = price.get("value", Decimal128::class.java).bigDecimalValue()
        val priceCurrency = Currency.valueOf(price.getString("unit"))

        val perUnit = source.get("unit", Document::class.java)
        val unitType = PricePerUnit.Unit.UnitType.valueOf(perUnit.getString("type"))

        val unitMeasurementName = perUnit.getString(("unit"))
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

        return PricePerUnit(
            price = Price(
                value = priceValue,
                unit = CurrencyUnit(currency = priceCurrency)
            ),
            unit = PricePerUnit.Unit(
                type = unitType,
                unit = unitMeasurement
            )
        )
    }
}