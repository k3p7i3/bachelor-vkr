package ru.hse.fcs.product.service.interfaces.dto.converter

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import ru.hse.fcs.product.service.domain.model.Product
import ru.hse.fcs.product.service.interfaces.dto.ProductDto
import ru.hse.fcs.product.service.interfaces.dto.ProductDto.*

@Component
class ProductToProductDtoConverter : Converter<Product, ProductDto> {
    override fun convert(source: Product) =
        ProductDto(
            id = source.id,
            isCustom = source.isCustom,
            externalId = source.externalId,
            skuId = source.skuId,
            skuParameters = source.skuParameters,
            marketplace = when (source.marketplace) {
                Product.Marketplace.TAOBAO -> Marketplace.TAOBAO
                Product.Marketplace.M1688 -> Marketplace.M1688
                Product.Marketplace.OTHER -> Marketplace.OTHER
            },
            productUrl = source.productUrl,
            imageUrl = source.imageUrl,
            title = source.title,
            weight = source.weight?.let { convertWeight(it) },
            boxVolume = source.boxVolume?.let { convertBoxVolume(it) },
            volume = source.volume?.let { convertVolume(it) },
            price = source.price?.let { convertPrice(it) }
        )

    private fun convertWeight(source: Product.Weight) =
        ProductDto.Weight(
            value = source.value,
            unit = when (source.unit) {
                Product.Weight.WeightUnit.KILOGRAM -> Weight.WeightUnit.KILOGRAM
                Product.Weight.WeightUnit.GRAM -> Weight.WeightUnit.GRAM
                Product.Weight.WeightUnit.TON -> Weight.WeightUnit.TON
                Product.Weight.WeightUnit.POUND -> Weight.WeightUnit.POUND
                Product.Weight.WeightUnit.OUNCE -> Weight.WeightUnit.OUNCE
            }
        )

    private fun convertBoxVolume(source: Product.BoxVolume) =
        ProductDto.BoxVolume(
            length = source.length,
            width = source.width,
            height = source.height,
            unit = when (source.unit) {
                Product.BoxVolume.LengthUnit.METRE -> BoxVolume.LengthUnit.METRE
                Product.BoxVolume.LengthUnit.CENTIMETRE -> BoxVolume.LengthUnit.CENTIMETRE
                Product.BoxVolume.LengthUnit.MILLIMETRE -> BoxVolume.LengthUnit.MILLIMETRE
                Product.BoxVolume.LengthUnit.FOOT -> BoxVolume.LengthUnit.FOOT
                Product.BoxVolume.LengthUnit.INCH -> BoxVolume.LengthUnit.INCH
            }
        )

    private fun convertVolume(source: Product.Volume) =
        ProductDto.Volume(
            value = source.value,
            unit = when (source.unit) {
                Product.Volume.VolumeUnit.CUBIC_METER -> Volume.VolumeUnit.CUBIC_METER
                Product.Volume.VolumeUnit.LITER -> Volume.VolumeUnit.LITER
                Product.Volume.VolumeUnit.MILLILITER -> Volume.VolumeUnit.MILLILITER
                Product.Volume.VolumeUnit.GALLON -> Volume.VolumeUnit.GALLON
                Product.Volume.VolumeUnit.QUART -> Volume.VolumeUnit.QUART
                Product.Volume.VolumeUnit.CUBIC_FOOT -> Volume.VolumeUnit.CUBIC_FOOT
            }
        )

    private fun convertPrice(source: Product.Price) =
        ProductDto.Price(
            value = source.value,
            currency = when (source.currency) {
                Product.Price.Currency.RUB -> Price.Currency.RUB
                Product.Price.Currency.EUR -> Price.Currency.EUR
                Product.Price.Currency.CNY -> Price.Currency.CNY
                Product.Price.Currency.USD -> Price.Currency.USD
            }
        )
}