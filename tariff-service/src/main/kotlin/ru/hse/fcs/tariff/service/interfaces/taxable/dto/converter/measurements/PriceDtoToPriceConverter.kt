package ru.hse.fcs.tariff.service.interfaces.taxable.dto.converter.measurements

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import ru.hse.fcs.tariff.service.domain.measure.Price
import ru.hse.fcs.tariff.service.domain.measure.unit.Currency
import ru.hse.fcs.tariff.service.domain.measure.unit.CurrencyUnit
import ru.hse.fcs.tariff.service.interfaces.taxable.dto.PriceDto

@Component
class PriceDtoToPriceConverter: Converter<PriceDto, Price> {
    override fun convert(source: PriceDto): Price {
        return Price(
            value = source.value,
            unit = CurrencyUnit(
                currency = when (source.currency) {
                    PriceDto.CurrencyDto.RUB -> Currency.RUB
                    PriceDto.CurrencyDto.CNY -> Currency.CNY
                    PriceDto.CurrencyDto.USD -> Currency.USD
                    PriceDto.CurrencyDto.EUR -> Currency.EUR
                }
            )
        )
    }
}