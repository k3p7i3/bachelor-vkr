package ru.hse.fcs.tariff.service.domain.tariff

import ru.hse.fcs.tariff.service.domain.taxable.TaxableParcel
import ru.hse.fcs.tariff.service.domain.taxable.Cost
import java.util.UUID

data class TariffItem(
    val tariffItemId: UUID? = null,
    val pricePerUnit: PricePerUnit,
    val conditions: List<TariffCondition>
) {
    fun isMatching(taxable: TaxableParcel): Boolean {
        return conditions.all { it.isMatching(taxable) }
    }

    fun validate(featuresNumber: Long): Boolean {
        val numericConditions = conditions.filterIsInstance<TariffNumericCondition>()
        val numericIsInvalid = numericConditions
            .groupBy { it.measurementUnit.type }
            .any { it.value.size > 1 }

        val typeConditions = conditions.filterIsInstance<TariffTypeCondition>()
        val typeIsInvalid = (typeConditions.groupBy { it.typeId }.count() < featuresNumber) ||
                (typeConditions.distinctBy { it.optionId }.size != typeConditions.size)

        return numericIsInvalid.not() && typeIsInvalid.not()
    }

    fun calculatePrice(taxable: TaxableParcel): Cost? {
        return pricePerUnit.calculateCost(taxable)
    }
}