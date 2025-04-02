package ru.hse.fcs.tariff.service.infrastructure

import com.mongodb.MongoClientSettings
import org.bson.UuidRepresentation
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration
import org.springframework.data.mongodb.core.convert.MongoCustomConversions
import ru.hse.fcs.tariff.service.domain.measure.unit.CurrencyUnitFactory
import ru.hse.fcs.tariff.service.infrastructure.dto.converter.NumericConditionMeasurementReadingConverter
import ru.hse.fcs.tariff.service.infrastructure.dto.converter.NumericConditionMeasurementWritingConverter
import ru.hse.fcs.tariff.service.infrastructure.dto.converter.UnitReadingConverter
import ru.hse.fcs.tariff.service.infrastructure.dto.converter.UnitWritingConverter

@Configuration
class MongoClientConfiguration(
    private val currencyUnitFactory: CurrencyUnitFactory
) : AbstractMongoClientConfiguration() {

    override fun getDatabaseName(): String = "tariff"

    public override fun configureClientSettings(builder: MongoClientSettings.Builder) {
        builder.uuidRepresentation(UuidRepresentation.STANDARD)
    }

    override fun configureConverters(
        converterConfigurationAdapter: MongoCustomConversions.MongoConverterConfigurationAdapter
    ) {
        converterConfigurationAdapter.registerConverters(
            listOf(
                // TODO test and change converters as needed!!
                NumericConditionMeasurementReadingConverter(currencyUnitFactory),
                NumericConditionMeasurementWritingConverter(),
                UnitReadingConverter(),
                UnitWritingConverter()
            )
        )
    }
}