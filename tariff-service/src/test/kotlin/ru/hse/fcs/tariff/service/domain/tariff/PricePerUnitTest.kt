package ru.hse.fcs.tariff.service.domain.tariff

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import ru.hse.fcs.tariff.service.domain.measure.Price
import ru.hse.fcs.tariff.service.domain.measure.Volume
import ru.hse.fcs.tariff.service.domain.measure.Weight
import ru.hse.fcs.tariff.service.domain.measure.unit.Currency
import ru.hse.fcs.tariff.service.domain.measure.unit.CurrencyUnit
import ru.hse.fcs.tariff.service.domain.measure.unit.VolumeUnit
import ru.hse.fcs.tariff.service.domain.measure.unit.WeightUnit
import ru.hse.fcs.tariff.service.domain.taxable.AppliedTariffData
import ru.hse.fcs.tariff.service.domain.taxable.Product
import ru.hse.fcs.tariff.service.domain.taxable.TaxableOrder
import ru.hse.fcs.tariff.service.domain.taxable.exception.NotEnoughOrderDataException
import java.math.BigDecimal
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@ExtendWith(MockitoExtension::class)
class PricePerUnitTest {
    val currencyRates = mapOf(
        (Currency.USD to Currency.RUB) to "100".toBigDecimal(),
        (Currency.RUB to Currency.USD) to "0.01".toBigDecimal(),
        (Currency.USD to Currency.USD) to BigDecimal.ONE,
        (Currency.RUB to Currency.RUB) to BigDecimal.ONE,
    )

    val taxable = TaxableOrder(
        products = listOf(
            Product(totalNumber = 5),
            Product(totalNumber = 3)
        ),
        weight = Weight(
            value = "3200".toBigDecimal(),
            unit = WeightUnit.GRAM
        ),
        volume = Volume(
            value = "3200".toBigDecimal(),
            unit = VolumeUnit.MILLILITER
        ),
        price = Price(
            value = "100000".toBigDecimal(),
            unit = CurrencyUnit(
                currency = Currency.RUB,
                currencyRates
            )
        ),
        appliedTariff = AppliedTariffData(
            tariffId = UUID.randomUUID(),
            finalCurrency = Currency.RUB
        )
    )

    @Test
    fun `calculateCost per weight unit`() {
        val pricePerUnit = PricePerUnit(
            price = Price(
                value = "5.5".toBigDecimal(),
                unit = CurrencyUnit(
                    currency = Currency.USD,
                    exchangeRates = currencyRates
                )
            ),
            unit = PricePerUnit.Unit(
                type = PricePerUnit.Unit.UnitType.WEIGHT,
                unit = WeightUnit.KILOGRAM
            )
        )

        val result = pricePerUnit.calculateCost(taxable)
        assert(result!!.cost.value.compareTo("17.6".toBigDecimal()) == 0)
        assertEquals(Currency.USD, result.cost.unit.currency)
        assert(result.unitAmount.compareTo("3.2".toBigDecimal()) == 0)
        assert(result.resultCost.value.compareTo("1760".toBigDecimal()) == 0)
        assertEquals(Currency.RUB, result.resultCost.unit.currency)
    }

    @Test
    fun  `calculateCost per volume unit`() {
        val pricePerUnit = PricePerUnit(
            price = Price(
                value = "5.5".toBigDecimal(),
                unit = CurrencyUnit(
                    currency = Currency.USD,
                    exchangeRates = currencyRates
                )
            ),
            unit = PricePerUnit.Unit(
                type = PricePerUnit.Unit.UnitType.VOLUME,
                unit = VolumeUnit.LITER
            )
        )

        val result = pricePerUnit.calculateCost(taxable)
        assert(result!!.cost.value.compareTo("17.6".toBigDecimal()) == 0)
        assertEquals(Currency.USD, result.cost.unit.currency)
        assert(result.unitAmount.compareTo("3.2".toBigDecimal()) == 0)
        assert(result.resultCost.value.compareTo("1760".toBigDecimal()) == 0)
        assertEquals(Currency.RUB, result.resultCost.unit.currency)
    }

    @Test
    fun  `calculateCost per product item`() {
        val pricePerUnit = PricePerUnit(
            price = Price(
                value = "5.5".toBigDecimal(),
                unit = CurrencyUnit(
                    currency = Currency.USD,
                    exchangeRates = currencyRates
                )
            ),
            unit = PricePerUnit.Unit(
                type = PricePerUnit.Unit.UnitType.PRODUCT_NUM
            )
        )
        val result = pricePerUnit.calculateCost(taxable)
        assert(result!!.cost.value.compareTo("44".toBigDecimal()) == 0)
        assertEquals(Currency.USD, result.cost.unit.currency)
        assert(result.unitAmount.compareTo("8".toBigDecimal()) == 0)
        assert(result.resultCost.value.compareTo("4400".toBigDecimal()) == 0)
        assertEquals(Currency.RUB, result.resultCost.unit.currency)
    }

    @Test
    fun  `calculateCost per unique product`() {
        val pricePerUnit = PricePerUnit(
            price = Price(
                value = "5.5".toBigDecimal(),
                unit = CurrencyUnit(
                    currency = Currency.USD,
                    exchangeRates = currencyRates
                )
            ),
            unit = PricePerUnit.Unit(
                type = PricePerUnit.Unit.UnitType.PRODUCT_TYPE_UNIT
            )
        )
        val result = pricePerUnit.calculateCost(taxable)
        assert(result!!.cost.value.compareTo("11".toBigDecimal()) == 0)
        assertEquals(Currency.USD, result.cost.unit.currency)
        assert(result.unitAmount.compareTo("2".toBigDecimal()) == 0)
        assert(result.resultCost.value.compareTo("1100".toBigDecimal()) == 0)
        assertEquals(Currency.RUB, result.resultCost.unit.currency)
    }

    @Test
    fun  `calculateCost with fixed price rate`() {
        val pricePerUnit = PricePerUnit(
            price = Price(
                value = "5.5".toBigDecimal(),
                unit = CurrencyUnit(
                    currency = Currency.USD,
                    exchangeRates = currencyRates
                )
            ),
            unit = PricePerUnit.Unit(
                type = PricePerUnit.Unit.UnitType.FIXED
            )
        )
        val result = pricePerUnit.calculateCost(taxable)
        assert(result!!.cost.value.compareTo("5.5".toBigDecimal()) == 0)
        assertEquals(Currency.USD, result.cost.unit.currency)
        assert(result.unitAmount.compareTo("1".toBigDecimal()) == 0)
        assert(result.resultCost.value.compareTo("550".toBigDecimal()) == 0)
        assertEquals(Currency.RUB, result.resultCost.unit.currency)
    }

    @Test
    fun  `calculateCost with percentage rate`() {
        val pricePerUnit = PricePerUnit(
            price = Price(
                value = "5".toBigDecimal(),
                unit = CurrencyUnit(
                    currency = Currency.USD,
                    exchangeRates = currencyRates
                )
            ),
            unit = PricePerUnit.Unit(
                type = PricePerUnit.Unit.UnitType.PERCENTAGE
            )
        )
        val result = pricePerUnit.calculateCost(taxable)
        assert(result!!.cost.value.compareTo("50".toBigDecimal()) == 0)
        assertEquals(Currency.USD, result.cost.unit.currency)
        assert(result.unitAmount.compareTo("1000".toBigDecimal()) == 0)
        assert(result.resultCost.value.compareTo("5000".toBigDecimal()) == 0)
        assertEquals(Currency.RUB, result.resultCost.unit.currency)
    }

    @Test
    fun `calculateCost - throw not enough data error`() {
        val pricePerUnit = PricePerUnit(
            price = Price(
                value = "5".toBigDecimal(),
                unit = CurrencyUnit(
                    currency = Currency.USD,
                    exchangeRates = currencyRates
                )
            ),
            unit = PricePerUnit.Unit(
                type = PricePerUnit.Unit.UnitType.WEIGHT,
                unit = WeightUnit.KILOGRAM
            )
        )
        val taxable = TaxableOrder(
            products = emptyList(),
            appliedTariff = AppliedTariffData(
                tariffId = UUID.randomUUID(),
                finalCurrency = Currency.RUB
            )
        )
        assertFailsWith<NotEnoughOrderDataException> {
            pricePerUnit.calculateCost(taxable)
        }
    }
}