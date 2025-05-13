package ru.hse.fcs.tariff.service.application

import org.springframework.stereotype.Component
import ru.hse.fcs.tariff.service.domain.measure.Price
import ru.hse.fcs.tariff.service.domain.measure.unit.Currency
import ru.hse.fcs.tariff.service.domain.tariff.TariffRepository
import ru.hse.fcs.tariff.service.domain.taxable.MultiTaxableOrder
import ru.hse.fcs.tariff.service.domain.taxable.TaxableOrder
import ru.hse.fcs.tariff.service.domain.taxable.TotalCost
import ru.hse.fcs.tariff.service.domain.taxable.exception.NotEnoughOrderDataException
import java.math.BigDecimal

@Component
class TaxableService(
    val tariffRepository: TariffRepository,
    val currencyService: CurrencyService
) {
    fun calculateTotalCost(order: MultiTaxableOrder): TotalCost {
        val currencyRates = getCurrencyExchangeRates(order)
        order.setCurrencyExchangeRates(rates = currencyRates)

        val taxableOrders = order.splitToTaxableOrders()

        val calculatedParcels = taxableOrders.mapNotNull { taxableOrder ->
            taxableOrder.setCurrencyExchangeRates(rates = currencyRates)
            val optionalTariff = tariffRepository.findById(
                taxableOrder.appliedTariff.tariffId
            )

            if (optionalTariff.isPresent) {
                val tariff = optionalTariff.get()
                tariff.setCurrencyExchangeRates(currencyRates)

                try {
                    val cost = tariff.calculateCost(taxableOrder)!!
                    TotalCost.CalculatedTaxableParcel(
                        taxableOrder = taxableOrder,
                        cost = cost
                    )
                } catch (ex: NotEnoughOrderDataException) {
                    TotalCost.CalculatedTaxableParcel(
                        taxableOrder = taxableOrder,
                        cost = null
                    )
                }
            } else null
        }

        return TotalCost(
            complexOrder = order,
            calculatedTaxableParcels = calculatedParcels,
            totalCost = calculatedParcels
                .map { it.cost?.resultCost }
                .plus(order.price)
                .filterNotNull()
                .reduceOrNull(Price::plus)

        )
    }

    fun calculateCost(order: TaxableOrder): TotalCost.CalculatedTaxableParcel {
        val optionalTariff = tariffRepository.findById(order.appliedTariff.tariffId)

        if (optionalTariff.isPresent) {
            val tariff = optionalTariff.get()
            val currencyRates = currencyService.getCurrencyExchangeRatesForTariff(tariff.agentId)

            tariff.setCurrencyExchangeRates(currencyRates)
            order.setCurrencyExchangeRates(currencyRates)

            try {
                val cost = tariff.calculateCost(order)!!
                return TotalCost.CalculatedTaxableParcel(
                    taxableOrder = order,
                    cost = cost
                )
            } catch (ex: NotEnoughOrderDataException) {
                return TotalCost.CalculatedTaxableParcel(
                    taxableOrder = order,
                    cost = null
                )
            }

        } else {
            throw RuntimeException("Tariff id=${order.appliedTariff.tariffId} was not found")
        }
    }

    private fun getCurrencyExchangeRates(order: MultiTaxableOrder):  Map<Pair<Currency, Currency>, BigDecimal> {
        val anyTariffId = listOfNotNull(
            order.appliedTariffs.firstOrNull(),
            order.taxableProducts.flatMap { it.appliedTariffs }.firstOrNull()
        ).first().tariffId
        val agentId = tariffRepository.findById(anyTariffId).get().agentId
        val currencyRates = currencyService.getCurrencyExchangeRatesForTariff(agentId)
        return currencyRates
    }
}