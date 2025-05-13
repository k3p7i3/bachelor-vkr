package ru.hse.fcs.product.service.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.util.*

@Table("product")
data class Product(

    @Id
    @Column("id")
    private var id: UUID? = null,

    @Column("is_custom")
    var isCustom: Boolean = false,

    @Column("external_id")
    var externalId: String? = null,

    @Column("sku_id")
    var skuId: String? = null,

    @Column("sku_parameters")
    var skuParameters: List<String>? = null,

    @Column("marketplace")
    var marketplace: Marketplace = Marketplace.OTHER,

    @Column("product_url")
    var productUrl: String,

    @Column("image_url")
    var imageUrl: String? = null,

    @Column("title")
    var title: String? = null,

    @Column("weight")
    var weight: Weight? = null,

    @Column("box_volume")
    var boxVolume: BoxVolume? = null,

    @Column("volume")
    var volume: Volume? = null,

    @Column("price")
    var price: Price? = null
): Persistable<UUID> {

    override fun getId(): UUID? = id

    override fun isNew(): Boolean = id == null

    fun setId(value: UUID) {
        id = value
    }

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