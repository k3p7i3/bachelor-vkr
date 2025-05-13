package ru.hse.fcs.tariff.service.interfaces.taxable.dto.converter

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import ru.hse.fcs.tariff.service.domain.measure.Density
import ru.hse.fcs.tariff.service.domain.measure.Price
import ru.hse.fcs.tariff.service.domain.measure.Volume
import ru.hse.fcs.tariff.service.domain.measure.Weight
import ru.hse.fcs.tariff.service.domain.taxable.AppliedTariffData
import ru.hse.fcs.tariff.service.domain.taxable.MultiTaxableProduct
import ru.hse.fcs.tariff.service.interfaces.taxable.dto.*

@Component
class MultiTaxableProductToProductDtoConverter(
    private val weightConverter: Converter<Weight, WeightDto>,
    private val volumeConverter: Converter<Volume, VolumeDto>,
    private val densityConverter: Converter<Density, DensityDto>,
    private val priceConverter: Converter<Price, PriceDto>,
    private val appliedTariffDataConverter: Converter<AppliedTariffData, AppliedTariffDataDto>
): Converter<MultiTaxableProduct, ProductDto> {

    override fun convert(source: MultiTaxableProduct): ProductDto? {
        return ProductDto(
            productId = source.productId,
            weight = source.weight?.let { weightConverter.convert(it) },
            volume = source.volume?.let {
                volumeConverter.convert(it)
            },
            density = source.density?.let {
                densityConverter.convert(it)
            },
            price = source.price?.let {
                priceConverter.convert(it)
            },
            totalNumber = source.totalNumber,
            appliedTariffs = source.appliedTariffs.map {
                appliedTariffDataConverter.convert(it)!!
            },
        )
    }
}