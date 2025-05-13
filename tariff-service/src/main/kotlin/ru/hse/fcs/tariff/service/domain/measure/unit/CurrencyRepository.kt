package ru.hse.fcs.tariff.service.domain.measure.unit

import java.util.*

interface CurrencyRepository {

    fun getCurrencyConversionRates(agentId: UUID): List<CurrencyExchangeRate>

    fun getCurrencyConversionRate(agentId: UUID, from: Currency, to: Currency): CurrencyExchangeRate?

    fun saveCurrencyConversionRate(agentId: UUID, currencyExchangeRate: CurrencyExchangeRate)

    fun saveCurrencyConversionRates(agentId: UUID, exchangeRates: List<CurrencyExchangeRate>): List<CurrencyExchangeRate>
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