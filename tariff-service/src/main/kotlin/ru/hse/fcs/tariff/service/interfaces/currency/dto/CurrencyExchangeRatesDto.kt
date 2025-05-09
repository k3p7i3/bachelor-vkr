package ru.hse.fcs.tariff.service.interfaces.currency.dto

import ru.hse.fcs.tariff.service.domain.measure.unit.CurrencyExchangeRate
import java.util.UUID

data class CurrencyExchangeRatesDto(
    val agentId: UUID,
    val exchangeRates: List<CurrencyExchangeRate>
)