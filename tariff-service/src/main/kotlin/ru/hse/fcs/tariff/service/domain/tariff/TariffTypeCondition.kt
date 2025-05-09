package ru.hse.fcs.tariff.service.domain.tariff

import org.springframework.data.annotation.TypeAlias
import ru.hse.fcs.tariff.service.domain.taxable.TaxableParcel
import java.util.*

@TypeAlias("type")
data class TariffTypeCondition(
    val typeId: UUID,
    val optionId: UUID
) : TariffCondition {

    override fun isMatching(taxable: TaxableParcel): Boolean {
        return taxable.appliedTariff.selectedCustomTypeOptions[typeId] == optionId
    }

    override fun isOverlapping(other: TariffCondition): Boolean =
        when (other) {
            is TariffTypeCondition -> typeId == other.typeId && optionId == other.optionId
            is TariffNumericCondition -> false
        }
}