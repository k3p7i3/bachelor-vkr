package ru.hse.fcs.tariff.service.infrastructure

import org.springframework.stereotype.Component
import ru.hse.fcs.tariff.service.domain.measure.unit.Currency
import ru.hse.fcs.tariff.service.domain.measure.unit.CurrencyExchangeRate
import ru.hse.fcs.tariff.service.domain.measure.unit.CurrencyRepository
import ru.hse.fcs.tariff.service.infrastructure.dto.CurrencyDto
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Component
class CurrencyRepositoryImpl(
    private val mongoCurrencyRepository: MongoCurrencyRepository
): CurrencyRepository {

    override fun getCurrencyConversionRates(agentId: UUID): List<CurrencyExchangeRate> {
        return mongoCurrencyRepository.findById(agentId).getOrNull()
            ?.convertToCurrencyExchangeRate()
            ?: emptyList()
    }

    override fun getCurrencyConversionRate(agentId: UUID, from: Currency, to: Currency): CurrencyExchangeRate? {
        return mongoCurrencyRepository.getRate(agentId, from, to).getOrNull()
            ?.bigDecimalValue()
            ?.let { CurrencyExchangeRate(from, to, it) }
    }

    override fun saveCurrencyConversionRate(agentId: UUID, currencyExchangeRate: CurrencyExchangeRate) {
        if (mongoCurrencyRepository.existsById(agentId)) {
            mongoCurrencyRepository.updateExchangeRate(
                agentId,
                currencyExchangeRate.from,
                currencyExchangeRate.to,
                currencyExchangeRate.rate
            )
        } else {
            mongoCurrencyRepository.insert(
                CurrencyDto(
                    agentId,
                    rates = mapOf(
                        currencyExchangeRate.from to
                            mapOf(
                                currencyExchangeRate.to to currencyExchangeRate.rate
                            )
                    )
                )
            )
        }
    }

    override fun saveCurrencyConversionRates(agentId: UUID, exchangeRates: List<CurrencyExchangeRate>): List<CurrencyExchangeRate>
        = mongoCurrencyRepository.save(
            CurrencyDto(
                agentId,
                rates = exchangeRates.groupBy { it.from }
                    .mapValues { ratesList ->
                        ratesList.value.associateBy(
                            { it.to }
                        ) { it.rate }
                    }
            )
        ).convertToCurrencyExchangeRate()
}