package ru.hse.fcs.tariff.service.infrastructure

import org.bson.types.Decimal128
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.Update
import ru.hse.fcs.tariff.service.domain.measure.unit.Currency
import ru.hse.fcs.tariff.service.infrastructure.dto.CurrencyDto
import java.math.BigDecimal
import java.util.*

interface MongoCurrencyRepository : MongoRepository<CurrencyDto, UUID> {

    @Query(value = "{ 'agentId': ?0 }", fields = "{ 'rates.?1.?2': 1 }")
    fun getRate(agentId: UUID, from: Currency, to: Currency): Optional<Decimal128>

    @Query("{ 'agentId': ?0 }")
    @Update("{ '\$set': { 'rates.?1.?2': ?3 } }")
    fun updateExchangeRate(agentId: UUID, from: Currency, to: Currency, rate: BigDecimal)
}

/*
* CurrencyConversion {
*   "agent-id": *value*,
*   "rates": {
*       "RUB": {
*           "USD": "123.0",
*           "CNY": "13.56"
*       }
*   }
* }
*
* */