package ru.hse.fcs.tariff.service.application

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import ru.hse.fcs.tariff.service.domain.measure.Price
import ru.hse.fcs.tariff.service.domain.measure.unit.Currency
import ru.hse.fcs.tariff.service.domain.measure.unit.CurrencyUnit
import ru.hse.fcs.tariff.service.domain.measure.unit.WeightUnit
import ru.hse.fcs.tariff.service.domain.tariff.PricePerUnit
import ru.hse.fcs.tariff.service.domain.tariff.Tariff
import ru.hse.fcs.tariff.service.domain.tariff.TariffItem
import ru.hse.fcs.tariff.service.domain.tariff.TariffNumericCondition
import ru.hse.fcs.tariff.service.domain.tariff.TariffRepository
import java.util.Optional
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@ExtendWith(MockitoExtension::class)
class TariffServiceImplTest {

    @Mock
    private lateinit var tariffRepository: TariffRepository

    @InjectMocks
    private lateinit var tariffService: TariffServiceImpl

    private val tariffId = UUID.randomUUID()
    private val agentId = UUID.randomUUID()
    private val testTariff = Tariff(
        tariffId = tariffId,
        agentId = agentId,
        title = "Test Tariff",
        applyLevel = Tariff.ApplyLevel.ORDER,
        tariffItems = listOf(
            TariffItem(
                conditions = listOf(
                    TariffNumericCondition(
                        measurementUnit = TariffNumericCondition.NumericConditionMeasurement(
                            type = TariffNumericCondition.NumericConditionMeasurement.Type.WEIGHT,
                            unit = WeightUnit.KILOGRAM
                        ),
                        minLimit = "10".toBigDecimal(),
                        maxLimit = "200".toBigDecimal()
                    )
                ),
                pricePerUnit = PricePerUnit(
                    price = Price(
                        value = "3".toBigDecimal(),
                        unit = CurrencyUnit(Currency.USD)
                    ),
                    unit = PricePerUnit.Unit(
                        type = PricePerUnit.Unit.UnitType.WEIGHT,
                        unit = WeightUnit.KILOGRAM
                    )
                )
            ),
            TariffItem(
                conditions = listOf(
                    TariffNumericCondition(
                        measurementUnit = TariffNumericCondition.NumericConditionMeasurement(
                            type = TariffNumericCondition.NumericConditionMeasurement.Type.WEIGHT,
                            unit = WeightUnit.KILOGRAM
                        ),
                        minLimit = "200".toBigDecimal(),
                        maxLimit = "500".toBigDecimal()
                    )
                ),
                pricePerUnit = PricePerUnit(
                    price = Price(
                        value = "2.5".toBigDecimal(),
                        unit = CurrencyUnit(Currency.USD)
                    ),
                    unit = PricePerUnit.Unit(
                        type = PricePerUnit.Unit.UnitType.WEIGHT,
                        unit = WeightUnit.KILOGRAM
                    )
                )
            ),
        )
    )

    @Test
    fun `createTariff should insert tariff`() {
        whenever(tariffRepository.insert(testTariff))
            .thenReturn(testTariff)
        val result = tariffService.createTariff(testTariff)
        assertEquals(testTariff, result)
        verify(tariffRepository).insert(testTariff)
    }

    @Test
    fun `updateTariff should save tariff`() {
        whenever(tariffRepository.save(testTariff)).thenReturn(testTariff)
        val result = tariffService.updateTariff(testTariff)
        assertEquals(testTariff, result)
        verify(tariffRepository).save(testTariff)
    }

    @Test
    fun `deleteTariff should delete by id`() {
        tariffService.deleteTariff(tariffId)
        verify(tariffRepository).deleteById(tariffId)
    }

    @Test
    fun `getTariff should return tariff when found`() {
        whenever(tariffRepository.findById(tariffId))
            .thenReturn(Optional.of(testTariff))
        val result = tariffService.getTariff(tariffId)
        assertEquals(testTariff, result)
    }

    @Test
    fun `getTariff should throw exception when not found`() {
        whenever(tariffRepository.findById(tariffId))
            .thenReturn(Optional.empty())
        assertFailsWith<NoSuchElementException> {
            tariffService.getTariff(tariffId)
        }
    }

    @Test
    fun `getAgentTariffs should return tariffs for agent`() {
        whenever(tariffRepository.findAllByAgentId(agentId))
            .thenReturn(listOf(testTariff))

        val result = tariffService.getAgentTariffs(agentId)
        assertEquals(1, result.size)
        assertEquals(testTariff, result[0])
    }

    @Test
    fun `getAgentsBriefTariffs should return brief tariffs for each agent`() {
        val agentIds = listOf(agentId)
        whenever(tariffRepository.findAllByAgentId(agentId))
            .thenReturn(listOf(testTariff))

        val result = tariffService.getAgentsBriefTariffs(agentIds)
        assertEquals(1, result.size)
        assertEquals(agentId, result[0].agentId)
        assertEquals(1, result[0].tariffs.size)
        assertEquals("Test Tariff", result[0].tariffs[0].title)
        assertEquals("2.5".toBigDecimal(), result[0].tariffs[0].price.price.value)
    }

    @Test
    fun `getAgentsBriefTariffs should handle empty tariff list`() {
        val agentIds = listOf(agentId)
        whenever(tariffRepository.findAllByAgentId(agentId))
            .thenReturn(emptyList())

        val result = tariffService.getAgentsBriefTariffs(agentIds)
        assertEquals(1, result.size)
        assertEquals(agentId, result[0].agentId)
        assertEquals(0, result[0].tariffs.size)
    }
}

