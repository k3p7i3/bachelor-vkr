package ru.hse.fcs.order.service.domain.model.measurement

data class Measurement<T>(
    val original: T,
    val normalized: T? = null
)