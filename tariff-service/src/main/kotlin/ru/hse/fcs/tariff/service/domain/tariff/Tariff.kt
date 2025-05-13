package ru.hse.fcs.tariff.service.domain.tariff

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import ru.hse.fcs.tariff.service.domain.measure.unit.Currency
import ru.hse.fcs.tariff.service.domain.measure.unit.CurrencyUnit
import ru.hse.fcs.tariff.service.domain.taxable.TaxableParcel
import ru.hse.fcs.tariff.service.domain.taxable.Cost
import ru.hse.fcs.tariff.service.domain.taxable.exception.NotEnoughOrderDataException
import java.math.BigDecimal
import java.util.UUID

@Document(collection = "tariffs")
data class Tariff(
    @Id
    var tariffId: UUID = UUID.randomUUID(),

    @Indexed
    val agentId: UUID,

    val applyLevel: ApplyLevel,

    val title: String,

    val description: String? = null,

    val features: List<TariffFeature> = emptyList(),

    val tariffItems: List<TariffItem> = emptyList(),

    val itemsGroupSizes: List<Long> = emptyList()
) {

    enum class ApplyLevel { ORDER, PRODUCT }

    fun calculateCost(taxable: TaxableParcel): Cost? {
        var cost: Cost? = null
        tariffItems.forEach { tariffItem ->
            if (tariffItem.isMatching(taxable)) {
                cost = tariffItem.calculateCost(taxable)
            }
        }
        if (cost == null) {
            throw NotEnoughOrderDataException()
        }
        return cost
    }

    fun setCurrencyExchangeRates(
        rates: Map<Pair<Currency, Currency>, BigDecimal>
    ) {
        tariffItems.forEach { tariffItem ->
            tariffItem.conditions
                .filter {
                    it is TariffNumericCondition &&
                            it.measurementUnit.type == TariffNumericCondition.NumericConditionMeasurement.Type.PRICE
                }
                .forEach { priceCondition ->
                    val currencyUnit = (priceCondition as TariffNumericCondition)
                        .measurementUnit.unit as CurrencyUnit
                    currencyUnit.exchangeRates = rates
                }

            tariffItem.pricePerUnit.price.unit.exchangeRates = rates
        }
    }
}
