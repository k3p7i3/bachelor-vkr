package ru.hse.fcs.tariff.service.domain.taxable

interface MultiTaxableParcel : Parcel {
    val appliedTariffs: List<AppliedTariffData>
}