package ru.hse.fcs.tariff.service.domain.taxable.exception

class CurrencyRateNotFound(
    override val message: String?
): Exception(message)