package ru.hse.fcs.tariff.service.interfaces.currency

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.hse.fcs.tariff.service.application.CurrencyService
import ru.hse.fcs.tariff.service.interfaces.currency.dto.CurrencyExchangeRatesDto
import java.util.*


@RestController
@RequestMapping("api/currency")
class  CurrencyController(
    val currencyService: CurrencyService
) {

    @GetMapping
    fun getAgentCurrencyRates(@RequestParam agentId: UUID): ResponseEntity<CurrencyExchangeRatesDto> {
        return ResponseEntity.ok(
            CurrencyExchangeRatesDto(
                agentId = agentId,
                exchangeRates = currencyService.getCurrencyExchangeRates(agentId)
            )
        )
    }

    @PostMapping()
    fun saveCurrencyRates(@RequestBody currencyExchangeRates: CurrencyExchangeRatesDto):
        ResponseEntity<CurrencyExchangeRatesDto> {

        val savedRates = currencyService.saveCurrencyRates(
            agentId = currencyExchangeRates.agentId,
            currencyExchangeRates = currencyExchangeRates.exchangeRates
        )
        return ResponseEntity.ok(
            CurrencyExchangeRatesDto(
                agentId = currencyExchangeRates.agentId,
                exchangeRates = savedRates
            )
        )
    }
}