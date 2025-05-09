package ru.hse.fcs.product.service.infrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.postgresql.util.PGobject
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing
import ru.hse.fcs.product.service.domain.model.Product

@Configuration
@EnableJdbcAuditing
class JdbcConfiguration(
    private val objectMapper: ObjectMapper
) : AbstractJdbcConfiguration() {

    override fun jdbcCustomConversions(): JdbcCustomConversions {
        return JdbcCustomConversions(
            mutableListOf(
                WeightReadingConverter(objectMapper),
                WeightWritingConverter(objectMapper),
                BoxVolumeReadingConverter(objectMapper),
                BoxVolumeWritingConverter(objectMapper),
                VolumeReadingConverter(objectMapper),
                VolumeWritingConverter(objectMapper),
                PriceReadingConverter(objectMapper),
                PriceWritingConverter(objectMapper)
            )
        )
    }

    @ReadingConverter
    class WeightReadingConverter(
        private val objectMapper: ObjectMapper
    ): Converter<PGobject, Product.Weight> {
        override fun convert(source: PGobject): Product.Weight {
            return objectMapper.readValue(
                source.value,
                Product.Weight::class.java
            )
        }
    }

    @WritingConverter
    class WeightWritingConverter(
        private val objectMapper: ObjectMapper
    ) : Converter<Product.Weight, PGobject> {
        override fun convert(source: Product.Weight): PGobject {
            val jsonObj = PGobject()
            jsonObj.type = "json"
            jsonObj.value = objectMapper.writeValueAsString(source)
            return jsonObj
        }
    }

    @ReadingConverter
    class BoxVolumeReadingConverter(
        private val objectMapper: ObjectMapper
    ): Converter<PGobject, Product.BoxVolume> {
        override fun convert(source: PGobject): Product.BoxVolume {
            return objectMapper.readValue(
                source.value,
                Product.BoxVolume::class.java
            )
        }
    }

    @WritingConverter
    class BoxVolumeWritingConverter(
        private val objectMapper: ObjectMapper
    ) : Converter<Product.BoxVolume, PGobject> {
        override fun convert(source: Product.BoxVolume): PGobject {
            val jsonObj = PGobject()
            jsonObj.type = "json"
            jsonObj.value = objectMapper.writeValueAsString(source)
            return jsonObj
        }
    }

    @ReadingConverter
    class VolumeReadingConverter(
        private val objectMapper: ObjectMapper
    ): Converter<PGobject, Product.Volume> {
        override fun convert(source: PGobject): Product.Volume {
            return objectMapper.readValue(
                source.value,
                Product.Volume::class.java
            )
        }
    }

    @WritingConverter
    class VolumeWritingConverter(
        private val objectMapper: ObjectMapper
    ) : Converter<Product.Volume, PGobject> {
        override fun convert(source: Product.Volume): PGobject {
            val jsonObj = PGobject()
            jsonObj.type = "json"
            jsonObj.value = objectMapper.writeValueAsString(source)
            return jsonObj
        }
    }

    @ReadingConverter
    class PriceReadingConverter(
        private val objectMapper: ObjectMapper
    ): Converter<PGobject, Product.Price> {
        override fun convert(source: PGobject): Product.Price {
            return objectMapper.readValue(
                source.value,
                Product.Price::class.java
            )
        }
    }

    @WritingConverter
    class PriceWritingConverter(
        private val objectMapper: ObjectMapper
    ) : Converter<Product.Price, PGobject> {
        override fun convert(source: Product.Price): PGobject {
            val jsonObj = PGobject()
            jsonObj.type = "json"
            jsonObj.value = objectMapper.writeValueAsString(source)
            return jsonObj
        }
    }
}