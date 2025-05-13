package ru.hse.fcs.product.service.interfaces.dto.converter

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import ru.hse.fcs.product.service.domain.model.Product
import ru.hse.fcs.product.service.domain.model.Product.*
import ru.hse.fcs.product.service.interfaces.dto.ProductDto

@Component
class ProductDtoToProductConverter : Converter<ProductDto, Product> {

    override fun convert(source: ProductDto) =
        Product(
            id = source.id,
            isCustom = source.isCustom,
            externalId = source.externalId,
            skuId = source.skuId,
            skuParameters = source.skuParameters,
            marketplace = when (source.marketplace) {
                ProductDto.Marketplace.TAOBAO -> Product.Marketplace.TAOBAO
                ProductDto.Marketplace.M1688 -> Product.Marketplace.M1688
                ProductDto.Marketplace.OTHER -> Product.Marketplace.OTHER
                else -> Product.Marketplace.OTHER
            },
            productUrl = source.productUrl,
            imageUrl = source.imageUrl,
            title = source.title,
            weight = source.weight?.let { convertWeight(source.weight) },
            boxVolume = source.boxVolume?.let { convertBoxVolume(source.boxVolume) },
            volume = source.volume?.let { convertVolume(source.volume) },
            price = source.price?.let { convertPrice(source.price) }
        )

    private fun convertWeight(source: ProductDto.Weight) =
        Product.Weight(
            value = source.value,
            unit = when (source.unit) {
                ProductDto.Weight.WeightUnit.KILOGRAM -> Weight.WeightUnit.KILOGRAM
                ProductDto.Weight.WeightUnit.GRAM -> Weight.WeightUnit.GRAM
                ProductDto.Weight.WeightUnit.TON -> Weight.WeightUnit.TON
                ProductDto.Weight.WeightUnit.POUND -> Weight.WeightUnit.POUND
                ProductDto.Weight.WeightUnit.OUNCE -> Weight.WeightUnit.OUNCE
            }
        )

    private fun convertBoxVolume(source: ProductDto.BoxVolume) =
        Product.BoxVolume(
            length = source.length,
            width = source.width,
            height = source.height,
            unit = when (source.unit) {
                ProductDto.BoxVolume.LengthUnit.METRE -> BoxVolume.LengthUnit.METRE
                ProductDto.BoxVolume.LengthUnit.CENTIMETRE -> BoxVolume.LengthUnit.CENTIMETRE
                ProductDto.BoxVolume.LengthUnit.MILLIMETRE -> BoxVolume.LengthUnit.MILLIMETRE
                ProductDto.BoxVolume.LengthUnit.FOOT -> BoxVolume.LengthUnit.FOOT
                ProductDto.BoxVolume.LengthUnit.INCH -> BoxVolume.LengthUnit.INCH
            }
        )

    private fun convertVolume(source: ProductDto.Volume) =
        Product.Volume(
            value = source.value,
            unit = when (source.unit) {
                ProductDto.Volume.VolumeUnit.CUBIC_METER -> Volume.VolumeUnit.CUBIC_METER
                ProductDto.Volume.VolumeUnit.LITER -> Volume.VolumeUnit.LITER
                ProductDto.Volume.VolumeUnit.MILLILITER -> Volume.VolumeUnit.MILLILITER
                ProductDto.Volume.VolumeUnit.GALLON -> Volume.VolumeUnit.GALLON
                ProductDto.Volume.VolumeUnit.QUART -> Volume.VolumeUnit.QUART
                ProductDto.Volume.VolumeUnit.CUBIC_FOOT -> Volume.VolumeUnit.CUBIC_FOOT
            }
        )

    private fun convertPrice(source: ProductDto.Price) =
        Product.Price(
            value = source.value,
            currency = when (source.currency) {
                ProductDto.Price.Currency.RUB -> Price.Currency.RUB
                ProductDto.Price.Currency.EUR -> Price.Currency.EUR
                ProductDto.Price.Currency.CNY -> Price.Currency.CNY
                ProductDto.Price.Currency.USD -> Price.Currency.USD
            }
        )
}