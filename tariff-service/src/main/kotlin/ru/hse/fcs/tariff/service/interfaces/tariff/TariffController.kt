package ru.hse.fcs.tariff.service.interfaces.tariff

import org.springframework.core.convert.converter.Converter
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.hse.fcs.tariff.service.application.TariffService
import ru.hse.fcs.tariff.service.domain.tariff.AgentBriefTariffs
import ru.hse.fcs.tariff.service.domain.tariff.Tariff
import ru.hse.fcs.tariff.service.interfaces.tariff.dto.AgentBriefTariffsDto
import ru.hse.fcs.tariff.service.interfaces.tariff.dto.TariffDto
import ru.hse.fcs.tariff.service.interfaces.tariff.dto.converter.PricePerUnitToTariffPriceDtoConverter
import java.util.*


@RestController
@RequestMapping("/api/tariff")
class TariffController(
    val tariffService: TariffService,
    val fromDtoConverter: Converter<TariffDto, Tariff>,
    val toDtoConverter: Converter<Tariff, TariffDto>,
    val priceConverter: PricePerUnitToTariffPriceDtoConverter
) {

    @GetMapping
    fun getAgentTariffs(@RequestParam agentId: UUID): ResponseEntity<List<TariffDto>> {
        return ResponseEntity.ok(
            tariffService.getAgentTariffs(agentId)
                .mapNotNull(toDtoConverter::convert)
        )
    }

    @PostMapping("/brief")
    fun getAgentsBriefTariffs(@RequestBody agentIds: List<UUID>): ResponseEntity<List<AgentBriefTariffsDto>> {
        return ResponseEntity.ok(
            tariffService.getAgentsBriefTariffs(agentIds)
                .map {
                    AgentBriefTariffsDto(
                        agentId = it.agentId,
                        tariffs = it.tariffs.map { t ->
                            AgentBriefTariffsDto.BriefTariff(
                                title = t.title,
                                price = priceConverter.convert(t.price)
                            )
                        }
                    )
                }
        )
    }

    @GetMapping("/{id}")
    fun getTariffById(@PathVariable id: UUID): ResponseEntity<TariffDto> {
        val tariff = tariffService.getTariff(id)
        return ResponseEntity.ok(toDtoConverter.convert(tariff))
    }

    @PostMapping
    fun createTariff(@RequestBody tariff: TariffDto): ResponseEntity<TariffDto> {
        val savedTariff = tariffService.createTariff(fromDtoConverter.convert(tariff)!!)
        return ResponseEntity.ok(
            toDtoConverter.convert(savedTariff)
        )
    }

    @PutMapping
    fun updateTariff(@RequestBody tariff: TariffDto): ResponseEntity<TariffDto> {
        val savedTariff = tariffService.updateTariff(fromDtoConverter.convert(tariff)!!)
        return ResponseEntity.ok(
            toDtoConverter.convert(savedTariff)
        )
    }

    @DeleteMapping("/{id}")
    fun deleteTariff(@PathVariable id: UUID): ResponseEntity<Void> {
        tariffService.deleteTariff(id)
        return ResponseEntity.noContent().build()
    }
}