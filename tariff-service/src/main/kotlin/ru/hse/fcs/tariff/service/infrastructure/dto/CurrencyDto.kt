package ru.hse.fcs.tariff.service.infrastructure.dto

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import ru.hse.fcs.tariff.service.domain.measure.unit.Currency
import ru.hse.fcs.tariff.service.domain.measure.unit.CurrencyExchangeRate
import java.math.BigDecimal
import java.util.*

@Document(collection = "currency_rates")
class CurrencyDto(
    @Id
    val agentId: UUID,
    val rates: Map<Currency, Map<Currency, BigDecimal>>
) {

    fun convertToCurrencyExchangeRate(): List<CurrencyExchangeRate> {
        return this.rates.entries
            .flatMap { (from, rates) ->
                rates.entries.map { (to, rate) ->
                    CurrencyExchangeRate(from, to, rate)
                }
            }
    }
}