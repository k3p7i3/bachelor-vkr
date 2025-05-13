package ru.hse.fcs.auth.service.domain

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails =
        username?.let {
            userRepository.findByEmail(username).orElseThrow {
                UsernameNotFoundException(
                    "User with email $username does not exists"
                )
            }
        } ?: throw UsernameNotFoundException("Username must not be empty")
}