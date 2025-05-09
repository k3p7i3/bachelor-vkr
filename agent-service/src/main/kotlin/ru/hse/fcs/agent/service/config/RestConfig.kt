package ru.hse.fcs.agent.service.config

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import java.time.Duration

@Configuration
class RestConfig {
    @Bean
    fun restTemplate(restTemplateBuilder: RestTemplateBuilder): RestTemplate {
        return restTemplateBuilder
            .defaultHeader("Access-Control-Allow-Origin", "*")
            .defaultHeader(
                "Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS"
            )
            .setConnectTimeout(Duration.ofSeconds(600))
            .setReadTimeout(Duration.ofSeconds(600))
            .build()
    }
}