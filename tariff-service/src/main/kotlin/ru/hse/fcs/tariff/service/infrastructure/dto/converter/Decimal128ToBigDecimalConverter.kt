package ru.hse.fcs.tariff.service.infrastructure.dto.converter

import org.bson.types.Decimal128
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import java.math.BigDecimal

@ReadingConverter
class Decimal128ToBigDecimalConverter : Converter<Decimal128, BigDecimal> {
    override fun convert(source: Decimal128) = source.bigDecimalValue()
}