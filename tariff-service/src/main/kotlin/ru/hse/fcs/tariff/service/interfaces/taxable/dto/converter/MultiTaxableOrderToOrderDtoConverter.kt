package ru.hse.fcs.tariff.service.interfaces.taxable.dto.converter

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import ru.hse.fcs.tariff.service.domain.measure.Density
import ru.hse.fcs.tariff.service.domain.measure.Price
import ru.hse.fcs.tariff.service.domain.measure.Volume
import ru.hse.fcs.tariff.service.domain.measure.Weight
import ru.hse.fcs.tariff.service.domain.measure.unit.CurrencyUnit
import ru.hse.fcs.tariff.service.domain.measure.unit.DensityUnit
import ru.hse.fcs.tariff.service.domain.measure.unit.VolumeUnit
import ru.hse.fcs.tariff.service.domain.measure.unit.WeightUnit
import ru.hse.fcs.tariff.service.domain.taxable.AppliedTariffData
import ru.hse.fcs.tariff.service.domain.taxable.MultiTaxableOrder
import ru.hse.fcs.tariff.service.domain.taxable.MultiTaxableProduct
import ru.hse.fcs.tariff.service.interfaces.taxable.dto.*

@Component
class MultiTaxableOrderToOrderDtoConverter(
    private val weightConverter: Converter<Weight, WeightDto>,
    private val volumeConverter: Converter<Volume, VolumeDto>,
    private val densityConverter: Converter<Density, DensityDto>,
    private val priceConverter: Converter<Price, PriceDto>,
    private val productConverter: Converter<MultiTaxableProduct, ProductDto>,
    private val appliedTariffDataConverter: Converter<AppliedTariffData, AppliedTariffDataDto>
): Converter<MultiTaxableOrder, OrderDto> {

    override fun convert(source: MultiTaxableOrder): OrderDto {
        return OrderDto(
            orderId = source.orderId,
            weight = source.weight?.let {
                OrderDto.MeasurementDto(
                    original = weightConverter.convert(it)!!,
                    normalized = weightConverter.convert(it.convertTo(WeightUnit.DEFAULT))
                )
            },
            volume = source.volume?.let {
                OrderDto.MeasurementDto(
                    original = volumeConverter.convert(it)!!,
                    normalized = volumeConverter.convert(it.convertTo(VolumeUnit.DEFAULT) as Volume),
                )
            },
            density = source.density?.let {
                OrderDto.MeasurementDto(
                    original = densityConverter.convert(it)!!,
                    normalized = densityConverter.convert(it.convertTo(DensityUnit.DEFAULT) as Density)
                )
            },
            price = source.price?.let {
                val finalCurrency =  listOfNotNull(
                    source.appliedTariffs.firstOrNull()?.finalCurrency,
                    source.taxableProducts.flatMap { it.appliedTariffs }.firstOrNull()?.finalCurrency
                ).first()

                OrderDto.MeasurementDto(
                    original = priceConverter.convert(it)!!,
                    normalized = priceConverter.convert(
                        it.convertTo(
                            CurrencyUnit(
                                currency = finalCurrency,
                                exchangeRates = it.unit.exchangeRates
                            )
                        ) as Price
                    )
                )
            },
            totalNumber = source.totalNumber,
            appliedTariffs = source.appliedTariffs.map {
                appliedTariffDataConverter.convert(it)!!
            },
            products = source.taxableProducts.map {
                productConverter.convert(it)!!
            }
        )
    }
}