package ru.hse.fcs.tariff.service.domain.tariff

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import ru.hse.fcs.tariff.service.domain.taxable.TaxableParcel
import ru.hse.fcs.tariff.service.domain.taxable.Cost
import java.util.UUID

@Document(collection = "tariffs")
data class Tariff(
    @Id
    val tariffId: UUID? = null,

    @Indexed
    val agentId: UUID,

    val applyLevel: ApplyLevel,

    val title: String,

    val description: String? = null,

    val features: List<TariffFeature> = emptyList(),

    val tariffItems: List<TariffItem> = emptyList()
) {

    enum class ApplyLevel { ORDER, PRODUCT }

    fun calculateCost(taxable: TaxableParcel): Cost? {
        var cost: Cost? = null
        tariffItems.forEach { tariffItem ->
            if (tariffItem.isMatching(taxable)) {
                cost = tariffItem.calculatePrice(taxable)
            }
        }
        return cost
    }
}
