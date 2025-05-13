package ru.hse.fcs.tariff.service.interfaces.taxable.dto.converter.measurements

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import ru.hse.fcs.tariff.service.domain.measure.Price
import ru.hse.fcs.tariff.service.domain.measure.unit.Currency
import ru.hse.fcs.tariff.service.interfaces.taxable.dto.PriceDto
import java.math.RoundingMode

@Component
class PriceToPriceDtoConverter : Converter<Price, PriceDto> {
    override fun convert(source: Price): PriceDto {
        return PriceDto(
            value = source.value.setScale(2, RoundingMode.HALF_EVEN),
            currency = when (source.unit.currency) {
                Currency.RUB -> PriceDto.CurrencyDto.RUB
                Currency.CNY -> PriceDto.CurrencyDto.CNY
                Currency.USD -> PriceDto.CurrencyDto.USD
                Currency.EUR -> PriceDto.CurrencyDto.EUR
            }
        )
    }
}