package ru.hse.fcs.order.service.interfaces.dto.converter.measurements

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import ru.hse.fcs.order.service.domain.model.measurement.Price
import ru.hse.fcs.order.service.domain.model.measurement.Price.Currency
import ru.hse.fcs.order.service.interfaces.dto.measurements.PriceDto

@Component
class PriceToPriceDtoConverter : Converter<Price, PriceDto> {
    override fun convert(source: Price): PriceDto {
        return PriceDto(
            value = source.value,
            currency = when (source.currency) {
                Currency.RUB -> PriceDto.CurrencyDto.RUB
                Currency.CNY -> PriceDto.CurrencyDto.CNY
                Currency.USD -> PriceDto.CurrencyDto.USD
                Currency.EUR -> PriceDto.CurrencyDto.EUR
            }
        )
    }
}