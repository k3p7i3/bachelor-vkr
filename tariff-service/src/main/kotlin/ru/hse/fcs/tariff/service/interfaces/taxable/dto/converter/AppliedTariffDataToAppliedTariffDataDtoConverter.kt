package ru.hse.fcs.tariff.service.interfaces.taxable.dto.converter;

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import ru.hse.fcs.tariff.service.domain.measure.unit.Currency
import ru.hse.fcs.tariff.service.domain.taxable.AppliedTariffData
import ru.hse.fcs.tariff.service.interfaces.taxable.dto.AppliedTariffDataDto
import ru.hse.fcs.tariff.service.interfaces.taxable.dto.PriceDto

@Component
class AppliedTariffDataToAppliedTariffDataDtoConverter: Converter<AppliedTariffData, AppliedTariffDataDto> {
    override fun convert(source: AppliedTariffData): AppliedTariffDataDto? {
        return AppliedTariffDataDto(
            tariffId = source.tariffId,
            selectedCustomTypeOptions = source.selectedCustomTypeOptions.entries.map {
                AppliedTariffDataDto.SelectedOptionDto(
                    featureId = it.key,
                    optionId = it.value
                )
            },
            finalCurrency = when (source.finalCurrency) {
                Currency.RUB -> PriceDto.CurrencyDto.RUB
                Currency.CNY -> PriceDto.CurrencyDto.CNY
                Currency.USD -> PriceDto.CurrencyDto.USD
                Currency.EUR -> PriceDto.CurrencyDto.EUR
            }
        )
    }
}
