package ru.hse.fcs.product.service.interfaces.dto

import com.fasterxml.jackson.annotation.JsonValue
import java.math.BigDecimal
import java.util.UUID

data class ProductDto(
    val id: UUID? = null,
    val isCustom: Boolean = false,
    val externalId: String? = null,
    val skuId: String? = null,
    val skuParameters: List<String>? = null,
    val marketplace: Marketplace? = null,
    val productUrl: String,
    val imageUrl: String? = null,
    val title: String? = null,
    val weight: Weight? = null,
    val boxVolume: BoxVolume? = null,
    val volume: Volume? = null,
    val price: Price? = null
) {

    enum class Marketplace(
        @JsonValue val marketplaceName: String
    ) {
        TAOBAO("TaoBao"),
        M1688("1688"),
        OTHER("other")
    }

    data class Weight(
        val value: BigDecimal,
        val unit: WeightUnit
    ) {
        enum class WeightUnit { KILOGRAM, GRAM, TON, POUND, OUNCE }
    }

    data class Volume(
        val value: BigDecimal,
        val unit: VolumeUnit
    ) {
        enum class VolumeUnit { CUBIC_METER, LITER, MILLILITER, GALLON, QUART, CUBIC_FOOT }
    }

    data class BoxVolume(
        val height: BigDecimal,
        val length: BigDecimal,
        val width: BigDecimal,
        val unit: LengthUnit
    ) {
        enum class LengthUnit { METRE, CENTIMETRE, MILLIMETRE, FOOT, INCH }
    }

    data class Price(
        val value: BigDecimal,
        val currency: Currency
    ) {
        enum class Currency { RUB, EUR, CNY, USD }
    }
}