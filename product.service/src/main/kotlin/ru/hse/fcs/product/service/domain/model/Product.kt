package ru.hse.fcs.product.service.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.util.*

@Table("product")
data class Product(

    @Id
    @Column("id")
    val id: UUID? = null,

    @Column("external_id")
    val externalId: String? = null,

    @Column("sku_id")
    val skuId: String? = null,

    @Column("sku_parameters")
    val skuParameters: List<String>? = null,

    @Column("marketplace")
    val marketplace: Marketplace = Marketplace.OTHER,

    @Column("product_url")
    val productUrl: String,

    @Column("image_url")
    val imageUrl: String? = null,

    @Column("title")
    val title: String? = null,

    @Column("weight")
    val weight: Weight? = null,

    @Column("box_volume")
    val boxVolume: BoxVolume? = null,

    @Column("volume")
    val volume: Volume? = null,

    @Column("price")
    val price: Price? = null
) {

    enum class Marketplace {
        TAOBAO, M1688, OTHER
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