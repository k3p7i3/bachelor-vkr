package ru.hse.fcs.order.service.interfaces.dto.converter.measurements

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import ru.hse.fcs.order.service.domain.model.measurement.Price
import ru.hse.fcs.order.service.domain.model.measurement.Price.Currency
import ru.hse.fcs.order.service.interfaces.dto.measurements.PriceDto

@Component
class PriceDtoToPriceConverter: Converter<PriceDto, Price> {
    override fun convert(source: PriceDto): Price {
        return Price(
            value = source.value,
            currency = when (source.currency) {
                PriceDto.CurrencyDto.RUB -> Currency.RUB
                PriceDto.CurrencyDto.CNY -> Currency.CNY
                PriceDto.CurrencyDto.USD -> Currency.USD
                PriceDto.CurrencyDto.EUR -> Currency.EUR
            }

        )
    }
}