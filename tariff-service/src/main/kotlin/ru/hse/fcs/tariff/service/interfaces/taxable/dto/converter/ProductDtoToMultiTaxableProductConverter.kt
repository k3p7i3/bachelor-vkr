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
class ProductDtoToMultiTaxableProductConverter(
    private val weightConverter: Converter<WeightDto, Weight>,
    private val boxVolumeConverter: Converter<BoxVolumeDto, Volume.BoxVolume>,
    private val volumeConverter: Converter<VolumeDto, Volume>,
    private val densityConverter: Converter<DensityDto, Density>,
    private val priceConverter: Converter<PriceDto, Price>,
    private val appliedTariffDataConverter: Converter<AppliedTariffDataDto, AppliedTariffData>
): Converter<ProductDto, MultiTaxableProduct> {

    override fun convert(source: ProductDto): MultiTaxableProduct? {
        return MultiTaxableProduct(
            productId = source.productId,
            weight = source.weight?.let { weightConverter.convert(it) },
            volume = source.boxVolume?.let {
                Volume(boxVolumeConverter.convert(it)!!)
            }
                ?: source.volume?.let {
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