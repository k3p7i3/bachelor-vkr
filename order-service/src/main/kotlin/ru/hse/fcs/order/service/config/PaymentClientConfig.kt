package ru.hse.fcs.order.service.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import ru.hse.fcs.order.service.config.properties.PaymentClientProperties
import java.time.Duration


@Configuration
@EnableConfigurationProperties(PaymentClientProperties::class)
class PaymentClientConfig(
    private val properties: PaymentClientProperties
) {
    @Bean
    fun restTemplate(restTemplateBuilder: RestTemplateBuilder): RestTemplate {
        return restTemplateBuilder
            .defaultHeader("Access-Control-Allow-Origin", "*")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(
                "Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS"
            )
            .basicAuthentication(
                properties.credentials.accessKey,
                properties.credentials.secretKey
            )
            .readTimeout(Duration.ofSeconds(600))
            .connectTimeout(Duration.ofSeconds(600))
            .build()
    }

    @Bean
    fun webClient(webClientBuilder: WebClient.Builder): WebClient {
        return webClientBuilder
            .baseUrl(properties.url)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeaders { headers ->
                headers.setBasicAuth(
                    properties.credentials.accessKey,
                    properties.credentials.secretKey
                )
            }
            .clientConnector(
                ReactorClientHttpConnector(
                    HttpClient.create()
                        .responseTimeout(properties.polling.timeout)
                )
            )
            .build()
    }
}