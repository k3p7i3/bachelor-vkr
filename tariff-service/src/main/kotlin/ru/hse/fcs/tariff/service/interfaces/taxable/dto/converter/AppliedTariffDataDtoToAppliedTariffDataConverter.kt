package ru.hse.fcs.tariff.service.interfaces.taxable.dto.converter;

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import ru.hse.fcs.tariff.service.domain.measure.unit.Currency
import ru.hse.fcs.tariff.service.domain.taxable.AppliedTariffData
import ru.hse.fcs.tariff.service.interfaces.taxable.dto.AppliedTariffDataDto
import ru.hse.fcs.tariff.service.interfaces.taxable.dto.PriceDto

@Component
class AppliedTariffDataDtoToAppliedTariffDataConverter: Converter<AppliedTariffDataDto, AppliedTariffData> {
    override fun convert(source: AppliedTariffDataDto): AppliedTariffData? {
        return AppliedTariffData(
            tariffId = source.tariffId,
            selectedCustomTypeOptions = source.selectedCustomTypeOptions.associate { it.featureId to it.optionId },
            finalCurrency = when (source.finalCurrency) {
                PriceDto.CurrencyDto.RUB -> Currency.RUB
                PriceDto.CurrencyDto.CNY -> Currency.CNY
                PriceDto.CurrencyDto.USD -> Currency.USD
                PriceDto.CurrencyDto.EUR -> Currency.EUR
            }
        )
    }
}
