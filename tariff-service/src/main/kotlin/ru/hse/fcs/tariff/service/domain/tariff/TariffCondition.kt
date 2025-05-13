package ru.hse.fcs.tariff.service.domain.tariff

import ru.hse.fcs.tariff.service.domain.taxable.TaxableParcel

sealed interface TariffCondition {
    fun isMatching(taxable: TaxableParcel): Boolean

    fun isOverlapping(other: TariffCondition): Boolean
}