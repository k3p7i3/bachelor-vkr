package ru.hse.fcs.tariff.service.domain.taxable

interface TaxableParcel : Parcel {
    val appliedTariff: AppliedTariffData
}