package ru.hse.fcs.agent.service.infrastructure

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import ru.hse.fcs.agent.service.application.dto.UserDto
import ru.hse.fcs.agent.service.application.dto.UserRegister

@Component
class UserClient(
    private val restTemplate: RestTemplate,

    @Value("\${web.client.user.service.url}")
    private val url: String
) {

    fun registerClient(userData: UserRegister): UserDto? {
        val endpointUrl = "$url/register"
        val headers = HttpHeaders()
        val requestEntity = HttpEntity(userData, headers)

        val response = restTemplate.exchange(
            endpointUrl,
            HttpMethod.POST,
            requestEntity,
            UserDto::class.java
        )

        if (response.statusCode.isError) {
            throw RuntimeException("Couldn't register user")
        }
        return response.body
    }
}