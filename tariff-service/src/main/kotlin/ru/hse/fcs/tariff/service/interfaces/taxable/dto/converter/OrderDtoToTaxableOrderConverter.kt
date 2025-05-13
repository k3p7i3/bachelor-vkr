package ru.hse.fcs.tariff.service.interfaces.taxable.dto.converter

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import ru.hse.fcs.tariff.service.domain.measure.Density
import ru.hse.fcs.tariff.service.domain.measure.Price
import ru.hse.fcs.tariff.service.domain.measure.Volume
import ru.hse.fcs.tariff.service.domain.measure.Weight
import ru.hse.fcs.tariff.service.domain.taxable.*
import ru.hse.fcs.tariff.service.interfaces.taxable.dto.*

@Component
class OrderDtoToTaxableOrderConverter(
    private val weightConverter: Converter<WeightDto, Weight>,
    private val boxVolumeConverter: Converter<BoxVolumeDto, Volume.BoxVolume>,
    private val volumeConverter: Converter<VolumeDto, Volume>,
    private val densityConverter: Converter<DensityDto, Density>,
    private val priceConverter: Converter<PriceDto, Price>,
    private val productConverter: Converter<ProductDto, Product>,
    private val appliedTariffDataConverter: Converter<AppliedTariffDataDto, AppliedTariffData>
): Converter<OrderDto, TaxableOrder> {

    override fun convert(source: OrderDto): TaxableOrder {
        return TaxableOrder(
            orderId = source.orderId,
            weight = source.weight?.let { weightConverter.convert(it.original) },
            volume = source.boxVolume?.let {
                Volume(boxVolumeConverter.convert(it.original)!!)
            }
                ?: source.volume?.let {
                    volumeConverter.convert(it.original)
                },
            density = source.density?.let {
                densityConverter.convert(it.original)
            },
            price = source.price?.let {
                priceConverter.convert(it.original)
            },
            totalNumber = source.totalNumber,
            appliedTariff = appliedTariffDataConverter.convert(source.appliedTariffs.first())!!,
            products = source.products.map {
                productConverter.convert(it)!!
            }
        )
    }
}