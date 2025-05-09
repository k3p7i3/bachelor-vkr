package ru.hse.fcs.agent.service.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.hse.fcs.agent.service.config.properties.S3Properties
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.S3Configuration
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import java.net.URI
import java.time.Duration

@Configuration
@EnableConfigurationProperties(S3Properties::class)
class S3Config(
    private val s3Properties: S3Properties
) {
    @Bean
    fun s3Client(): S3Client {
        val builder = S3Client.builder()
            .credentialsProvider(createCredentialsProvider())
            .region(Region.of(s3Properties.region))
            .serviceConfiguration(createServiceConfiguration())
            .overrideConfiguration(createOverrideConfiguration())
            .endpointOverride(URI.create(s3Properties.endpointUrl))

        return builder.build()
    }

    @Bean
    fun s3Presigner(): S3Presigner {
        val builder = S3Presigner.builder()
            .credentialsProvider(createCredentialsProvider())
            .region(Region.of(s3Properties.region))
            .serviceConfiguration(createServiceConfiguration())
            .endpointOverride(URI.create(s3Properties.endpointUrl))

        return builder.build()
    }

    private fun createCredentialsProvider() = StaticCredentialsProvider.create(
        AwsBasicCredentials.create(
            s3Properties.credentials.accessKey,
            s3Properties.credentials.secretKey
        )
    )
    private fun createServiceConfiguration(): S3Configuration {
        return S3Configuration.builder()
            .pathStyleAccessEnabled(true)
            .build()
    }

    private fun createOverrideConfiguration(): ClientOverrideConfiguration {
        val builder = ClientOverrideConfiguration.builder()
        s3Properties.advanced?.let { advanced ->
            advanced.connectionTimeout?.let {
                builder.apiCallTimeout(Duration.ofMillis(it.toLong()))
            }
            advanced.requestTimeout?.let {
                builder.apiCallAttemptTimeout(Duration.ofMillis(it.toLong()))
            }
        }
        return builder.build()
    }
}