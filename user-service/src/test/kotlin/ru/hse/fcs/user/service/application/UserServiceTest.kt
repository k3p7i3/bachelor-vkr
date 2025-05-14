package ru.hse.fcs.user.service.application

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.client.HttpClientErrorException
import ru.hse.fcs.user.service.domain.User
import ru.hse.fcs.user.service.domain.UserRepository
import java.util.Optional
import java.util.UUID
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class UserServiceTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var passwordEncoder: PasswordEncoder

    @InjectMocks
    private lateinit var userService: UserService

    private lateinit var testUser: User

    @BeforeEach
    fun setUp() {
        testUser = User(
            id = UUID.randomUUID(),
            email = "test@example.com",
            password = "password",
            firstName = "Test",
            lastName = "User"
        )
    }

    @Test
    fun `signUp should save new user with encoded password`() {
        val clearPassword = testUser.password
        val encodedPassword = "encodedPassword"

        whenever(userRepository.existsByEmail(testUser.email))
            .thenReturn(false)
        whenever(passwordEncoder.encode(testUser.password))
            .thenReturn(encodedPassword)
        whenever(userRepository.save(any<User>()))
            .thenReturn(testUser)
        val result = userService.signUp(testUser)

        assertEquals(testUser, result)
        assertEquals(encodedPassword, testUser.password)
        verify(userRepository).existsByEmail(testUser.email)
        verify(passwordEncoder).encode(clearPassword)
        verify(userRepository).save(testUser)
    }

    @Test
    fun `signUp should throw exception when email is already taken`() {
        whenever(
            userRepository.existsByEmail(testUser.email)
        ).thenReturn(true)

        val exception = assertThrows<HttpClientErrorException> {
            userService.signUp(testUser)
        }

        assertEquals(HttpStatus.BAD_REQUEST, exception.statusCode)
        assertEquals("400 User with the email already exists", exception.message)
        verify(userRepository).existsByEmail(testUser.email)
        verifyNoMoreInteractions(userRepository, passwordEncoder)
    }

    @Test
    fun `getUser should return user when exists`() {
        whenever(userRepository.findByEmail(testUser.email))
            .thenReturn(Optional.of(testUser))

        val result = userService.getUser(testUser.email)

        assertEquals(testUser, result)
        verify(userRepository).findByEmail(testUser.email)
    }

    @Test
    fun `getUser should throw exception when user not found`() {
        whenever(userRepository.findByEmail(testUser.email))
            .thenReturn(Optional.empty())

        val exception = assertThrows<HttpClientErrorException> {
            userService.getUser(testUser.email)
        }
        assertEquals(HttpStatus.BAD_REQUEST, exception.statusCode)
        assertEquals("400 User does not exist", exception.message)
        verify(userRepository).findByEmail(testUser.email)
    }

    @Test
    fun `getUserById should return user when exists`() {
        whenever(userRepository.findById(testUser.id!!))
            .thenReturn(Optional.of(testUser))
        val result = userService.getUserById(testUser.id!!)
        assertEquals(testUser, result)
        verify(userRepository).findById(testUser.id!!)
    }

    @Test
    fun `getUserById should throw exception when user not found`() {
        whenever(userRepository.findById(testUser.id!!))
            .thenReturn(Optional.empty())

        val exception = assertThrows<HttpClientErrorException> {
            userService.getUserById(testUser.id!!)
        }
        assertEquals(HttpStatus.BAD_REQUEST, exception.statusCode)
        assertEquals("400 User does not exist", exception.message)
        verify(userRepository).findById(testUser.id!!)
    }

    @Test
    fun `updateUserInfo should update fields when they are provided and email is unique`() {
        val oldEmail = testUser.email
        val updatedFirstName = "New FirstName"
        val updatedLastName = "New LastName"
        val updatedEmail = "updated@example.com"

        whenever(userRepository.findByEmail(oldEmail))
            .thenReturn(Optional.of(testUser))
        whenever(userRepository.existsByEmail(updatedEmail))
            .thenReturn(false)
        whenever(userRepository.save(any<User>()))
            .thenAnswer { it.arguments[0] }

        val result = userService.updateUserInfo(
            username = testUser.email,
            firstName = updatedFirstName,
            lastName = updatedLastName,
            email = updatedEmail
        )

        assertEquals(updatedFirstName, result.firstName)
        assertEquals(updatedLastName, result.lastName)
        assertEquals(updatedEmail, result.email)
        verify(userRepository).findByEmail(oldEmail)
        verify(userRepository).existsByEmail(updatedEmail)
        verify(userRepository).save(testUser)
    }

    @Test
    fun `updateUserInfo should throw exception when trying to use existing email`() {
        val existingEmail = "existing@example.com"
        whenever(userRepository.findByEmail(testUser.email))
            .thenReturn(Optional.of(testUser))
        whenever(userRepository.existsByEmail(existingEmail))
            .thenReturn(true)

        val exception = assertThrows<HttpClientErrorException> {
            userService.updateUserInfo(
                username = testUser.email,
                firstName = null,
                lastName = null,
                email = existingEmail
            )
        }
        assertEquals(HttpStatus.BAD_REQUEST, exception.statusCode)
        assertEquals("400 User with email already exists", exception.message)
        verify(userRepository).findByEmail(testUser.email)
        verify(userRepository).existsByEmail(existingEmail)
        verifyNoMoreInteractions(userRepository)
    }

    @Test
    fun `updatePassword should update password when old password matches`() {
        val oldPassword = "oldPassword"
        val newPassword = "newPassword"
        val encodedNewPassword = "encodedNewPassword"
        val encodedOldPassword = "encodedOldPassword"
        testUser.password = encodedOldPassword

        whenever(userRepository.findByEmail(testUser.email))
            .thenReturn(Optional.of(testUser))
        whenever(passwordEncoder.matches(oldPassword, testUser.password))
            .thenReturn(true)
        whenever(passwordEncoder.encode(newPassword))
            .thenReturn(encodedNewPassword)
        whenever(userRepository.save(any<User>()))
            .thenAnswer { it.arguments[0] }

        val result = userService.updatePassword(
            username = testUser.email,
            oldPassword = oldPassword,
            newPassword = newPassword
        )

        assert(result)
        assertEquals(encodedNewPassword, testUser.password)
        verify(userRepository).findByEmail(testUser.email)
        verify(passwordEncoder).matches(oldPassword, encodedOldPassword)
        verify(passwordEncoder).encode(newPassword)
        verify(userRepository).save(testUser)
    }

    @Test
    fun `updatePassword should throw exception when old password is incorrect`() {
        val wrongOldPassword = "wrongPassword"
        whenever(userRepository.findByEmail(testUser.email))
            .thenReturn(Optional.of(testUser))
        whenever(
            passwordEncoder.matches(
                wrongOldPassword,
                testUser.password
            )
        ).thenReturn(false)

        val exception = assertThrows<HttpClientErrorException> {
            userService.updatePassword(
                username = testUser.email,
                oldPassword = wrongOldPassword,
                newPassword = "newPassword"
            )
        }
        assertEquals(HttpStatus.BAD_REQUEST, exception.statusCode)
        assertEquals("400 Incorrect old password", exception.message)
        verify(userRepository).findByEmail(testUser.email)
        verify(passwordEncoder).matches(wrongOldPassword, testUser.password)
        verifyNoMoreInteractions(passwordEncoder, userRepository)
    }

    @Test
    fun `updatePassword should throw exception when user not found`() {
        whenever(userRepository.findByEmail(testUser.email))
            .thenReturn(Optional.empty())

        val exception = assertThrows<HttpClientErrorException> {
            userService.updatePassword(
                username = testUser.email,
                oldPassword = "oldPassword",
                newPassword = "newPassword"
            )
        }
        assertEquals(HttpStatus.BAD_REQUEST, exception.statusCode)
        assertEquals("400 User does not exist", exception.message)
        verify(userRepository).findByEmail(testUser.email)
        verifyNoMoreInteractions(passwordEncoder, userRepository)
    }
}