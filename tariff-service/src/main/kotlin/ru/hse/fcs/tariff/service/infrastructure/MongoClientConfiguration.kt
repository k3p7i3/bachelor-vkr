package ru.hse.fcs.tariff.service.infrastructure

import com.mongodb.MongoClientSettings
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.bson.UuidRepresentation
import org.springframework.boot.autoconfigure.mongo.MongoProperties
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration
import org.springframework.data.mongodb.core.convert.MongoCustomConversions
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import ru.hse.fcs.tariff.service.infrastructure.dto.converter.*

@Configuration
@EnableMongoRepositories
class MongoClientConfiguration(
    private val mongoProperties: MongoProperties
) : AbstractMongoClientConfiguration() {

    override fun getDatabaseName() = mongoProperties.database

    override fun configureConverters(
        converterConfigurationAdapter: MongoCustomConversions.MongoConverterConfigurationAdapter
    ) {
        converterConfigurationAdapter.registerConverters(
            listOf(
                // TODO test and change converters as needed!!
                TariffConditionReadingConverter(),
                TariffConditionWritingConverter(),
                PricePerUnitReadingConverter(),
                PricePerUnitWritingConverter(),
                BigDecimalToDecimal128Converter(),
                Decimal128ToBigDecimalConverter()
            )
        )
    }

    override fun mongoClient(): MongoClient {
        val credential = MongoCredential.createCredential(
            mongoProperties.username,
            mongoProperties.authenticationDatabase ?: mongoProperties.database,
            mongoProperties.password
        )

        return MongoClients.create(
            MongoClientSettings.builder()
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .applyToClusterSettings { builder ->
                    builder.hosts(listOf(
                        ServerAddress(mongoProperties.host, mongoProperties.port)
                    ))
                }
                .credential(credential)
                .build()
        )
    }
}