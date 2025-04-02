package ru.hse.fcs.tariff.service.interfaces.taxable

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.hse.fcs.tariff.service.application.TaxableService
import ru.hse.fcs.tariff.service.domain.taxable.MultiTaxableOrder
import ru.hse.fcs.tariff.service.domain.taxable.TotalCost

@RestController
@RequestMapping("/tariff/order")
class TaxableController(
    val taxableService: TaxableService
) {

    @PostMapping("/calculate")
    fun calculateOrderCost(@RequestBody order: MultiTaxableOrder): ResponseEntity<TotalCost> {
        return ResponseEntity.ok(taxableService.calculateTotalCost(order))
    }
}