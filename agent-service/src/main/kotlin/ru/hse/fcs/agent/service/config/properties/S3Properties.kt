package ru.hse.fcs.agent.service.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix="s3")
data class S3Properties(
    val credentials: AwsCredentials,
    val endpointUrl: String,
    val region: String,
    val buckets: Buckets,
    val presignedUrlDuration: Long,
    val advanced: AdvancedSettings? = null
) {

    data class AwsCredentials(
        val accessKey: String,
        val secretKey: String,
    )

    data class Buckets(
        val agentProfileBucket: String
    )

    data class AdvancedSettings(
        val connectionTimeout: Int? = null,
        val requestTimeout: Int? = null,
        val clientExecutionTimeout: Int? = null
    )
}
