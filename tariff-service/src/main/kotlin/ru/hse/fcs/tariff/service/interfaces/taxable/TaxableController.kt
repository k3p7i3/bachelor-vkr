package ru.hse.fcs.tariff.service.interfaces.taxable

import org.springframework.core.convert.converter.Converter
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.hse.fcs.tariff.service.application.TaxableService
import ru.hse.fcs.tariff.service.domain.taxable.MultiTaxableOrder
import ru.hse.fcs.tariff.service.domain.taxable.TaxableOrder
import ru.hse.fcs.tariff.service.interfaces.taxable.dto.OrderDto
import ru.hse.fcs.tariff.service.interfaces.taxable.dto.TotalCostDto
import ru.hse.fcs.tariff.service.interfaces.taxable.dto.converter.TotalCostToTotalCostDtoConverter

@RestController
@RequestMapping("/api/taxable")
class TaxableController(
    val taxableService: TaxableService,
    private val fromComplexOrderDtoConverter: Converter<OrderDto, MultiTaxableOrder>,
    private val fromOrderDtoConverter: Converter<OrderDto, TaxableOrder>,
    private val costConverter: TotalCostToTotalCostDtoConverter
) {

    @PostMapping("/calculate/order")
    fun calculateOrderCost(@RequestBody order: OrderDto): ResponseEntity<TotalCostDto> {
        val multiTaxableOrder = fromComplexOrderDtoConverter.convert(order)!!
        val resultCost = taxableService.calculateTotalCost(multiTaxableOrder)
        return ResponseEntity.ok(costConverter.convert(resultCost))
    }

    @PostMapping("/calculate/service")
    fun calculateOrderServiceCost(@RequestBody order: OrderDto): ResponseEntity<TotalCostDto.CalculatedTariff> {
        val taxableOrder = fromOrderDtoConverter.convert(order)!!
        val resultCost = taxableService.calculateCost(taxableOrder)
        return ResponseEntity.ok(
            costConverter.convert(resultCost)
        )
    }
}