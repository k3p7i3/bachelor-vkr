package ru.hse.fcs.tariff.service.infrastructure

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.Update
import ru.hse.fcs.tariff.service.domain.measure.unit.Currency
import ru.hse.fcs.tariff.service.domain.measure.unit.CurrencyExchangeRate
import ru.hse.fcs.tariff.service.domain.measure.unit.CurrencyRepository
import ru.hse.fcs.tariff.service.infrastructure.dto.CurrencyDto
import java.math.BigDecimal
import java.util.*
import kotlin.jvm.optionals.getOrNull


interface MongoCurrencyRepository : MongoRepository<CurrencyDto, UUID>, CurrencyRepository {

    override fun getCurrencyConversionRates(agentId: UUID): List<CurrencyExchangeRate> {
        return this.findById(agentId).getOrNull()
            ?.convertToCurrencyExchangeRate()
            ?: emptyList()
    }

    override fun getCurrencyConversionRate(agentId: UUID, from: Currency, to: Currency): CurrencyExchangeRate? {
        return getRate(agentId, from, to).getOrNull()
            ?.let { CurrencyExchangeRate(from, to, it) }
    }

    override fun saveCurrencyConversionRate(agentId: UUID, currencyExchangeRate: CurrencyExchangeRate) {
        if (this.existsById(agentId)) {
            this.updateExchangeRate(
                agentId,
                currencyExchangeRate.from,
                currencyExchangeRate.to,
                currencyExchangeRate.rate
            )
        } else {
            this.insert(
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

    @Query(value = "{ 'agentId': ?0 }", fields = "{ 'rates.?1.?2': 1 }")
    fun getRate(agentId: UUID, from: Currency, to: Currency): Optional<BigDecimal>

    @Query("{ 'agentId': ?0 }")
    @Update("{ '\$set': { 'rates.?1.?2': ?3 } }")
    fun updateExchangeRate(agentId: UUID, from: Currency, to: Currency, rate: BigDecimal)
}

/*
* CurrencyConversion {
*   "agent-id": *value*,
*   "rates": {
*       "RUB": {
*           "USD": "123.0",
*           "CNY": "13.56"
*       }
*   }
* }
*
* */