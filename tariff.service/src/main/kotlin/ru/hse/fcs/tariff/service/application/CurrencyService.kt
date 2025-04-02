package ru.hse.fcs.tariff.service.application

import org.springframework.stereotype.Component
import ru.hse.fcs.tariff.service.domain.measure.unit.CurrencyExchangeRate
import ru.hse.fcs.tariff.service.domain.measure.unit.CurrencyRepository
import java.util.*

@Component
class CurrencyService(
    private val currencyRepository: CurrencyRepository
) {
    fun saveCurrencyRate(
        agentId: UUID,
        currencyExchangeRate: CurrencyExchangeRate
    ) {
        currencyRepository.saveCurrencyConversionRate(agentId, currencyExchangeRate)
    }

    fun getCurrencyExchangeRates(
        agentId: UUID
    ) = currencyRepository.getCurrencyConversionRates(agentId)
}