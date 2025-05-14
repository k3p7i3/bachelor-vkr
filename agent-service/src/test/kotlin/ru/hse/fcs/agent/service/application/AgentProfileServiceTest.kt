package ru.hse.fcs.agent.service.application

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import ru.hse.fcs.agent.service.application.dto.UserDto
import ru.hse.fcs.agent.service.application.dto.UserRegister
import ru.hse.fcs.agent.service.domain.model.AgentImage
import ru.hse.fcs.agent.service.domain.model.AgentProfile
import ru.hse.fcs.agent.service.domain.model.AgentRating
import ru.hse.fcs.agent.service.domain.repository.AgentProfileRepository
import ru.hse.fcs.agent.service.domain.repository.AgentRatingRepository
import ru.hse.fcs.agent.service.infrastructure.AgentImageRepository
import ru.hse.fcs.agent.service.infrastructure.UserClient
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.Optional
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class AgentProfileServiceTest {

    @Mock
    private lateinit var agentProfileRepository: AgentProfileRepository

    @Mock
    private lateinit var agentRatingRepository: AgentRatingRepository

    @Mock
    private lateinit var agentImageRepository: AgentImageRepository

    @Mock
    private lateinit var userClient: UserClient

    private lateinit var agentProfileService: AgentProfileService

    private val newAgentRecommendationScore = BigDecimal("3.5")
    private lateinit var testAgentProfile: AgentProfile
    private lateinit var testUserRegister: UserRegister
    private val testAgentId = UUID.randomUUID()

    @BeforeEach
    fun setUp() {
        agentProfileService = AgentProfileService(
            agentProfileRepository,
            agentRatingRepository,
            agentImageRepository,
            userClient,
            newAgentRecommendationScore
        )

        testAgentProfile = AgentProfile(
            id = testAgentId,
            name = "Test Agent",
            description = "Test Description",
            contactPhoneNumber = "+799999999999",
            contactEmail = "feedback@gmail.com"
        )

        testUserRegister = UserRegister(
            firstName = "name",
            lastName = "surname",
            email = "test@example.com",
            password = "password"
        )
    }

    @Test
    fun `createAgent should save agent profile, rating, create directory and register user`() {
        val savedAgentProfile = testAgentProfile.copy()
        val agentRating = AgentRating.createInitRating(testAgentId, newAgentRecommendationScore)
        val userDto = UserDto(
            UUID.randomUUID(),
            testUserRegister.firstName,
            testUserRegister.lastName,
            testUserRegister.email,
            role = UserDto.Role.AGENT_ADMIN,
            testAgentId
        )

        whenever(agentProfileRepository.save(testAgentProfile))
            .thenReturn(savedAgentProfile)
        whenever(agentRatingRepository.save(any<AgentRating>()))
            .thenReturn(agentRating)
        whenever(userClient.registerClient(any<UserRegister>()))
            .thenReturn(userDto)

        val (resultAgent, resultUser) = agentProfileService.createAgent(testAgentProfile, testUserRegister)

        assertEquals(savedAgentProfile, resultAgent.profile)
        assertEquals(agentRating, resultAgent.rating)
        assertEquals(userDto, resultUser)

        verify(agentProfileRepository).save(testAgentProfile)
        verify(agentRatingRepository).save(any<AgentRating>())
        verify(agentImageRepository).createDirectoryForAgent(testAgentId)
        verify(userClient).registerClient(testUserRegister.copy(agentId = testAgentId))
    }

    @Test
    fun `updateAgentProfile should save and return updated profile`() {
        val updatedProfile = testAgentProfile.copy(description = "Updated Description")
        whenever(agentProfileRepository.save(updatedProfile))
            .thenReturn(updatedProfile)

        val result = agentProfileService.updateAgentProfile(updatedProfile)
        assertEquals(updatedProfile, result)
        verify(agentProfileRepository).save(updatedProfile)
    }

    @Test
    fun `getAgentProfile should return profile when exists`() {
        whenever(agentProfileRepository.findById(testAgentId))
            .thenReturn(Optional.of(testAgentProfile))

        val result = agentProfileService.getAgentProfile(testAgentId)
        assertEquals(testAgentProfile, result)
        verify(agentProfileRepository).findById(testAgentId)
    }

    @Test
    fun `getAgent should return full agent details when exists`() {
        val agentRating = AgentRating(
            UUID.randomUUID(),
            testAgentId,
            gradesSum = 56,
            reviewsNumber = 13,
            recommendationScore = BigDecimal("4.5")
        )
        val agentImages = listOf(
            AgentImage(
                UUID.randomUUID(),
                testAgentId,
                "avatar.jpg",
                LocalDateTime.now(),
                true
            ),
            AgentImage(
                UUID.randomUUID(),
                testAgentId,
                "image1.jpg",
                LocalDateTime.now(),
                false
            ),
            AgentImage(
                UUID.randomUUID(),
                testAgentId,
                "image2.jpg",
                LocalDateTime.now(),
                false
            )
        )

        whenever(agentProfileRepository.findById(testAgentId))
            .thenReturn(Optional.of(testAgentProfile))
        whenever(agentRatingRepository.findByAgentId(testAgentId))
            .thenReturn(Optional.of(agentRating))
        whenever(agentImageRepository.getAgentImages(testAgentId))
            .thenReturn(agentImages)

        val result = agentProfileService.getAgent(testAgentId)
        assertEquals(testAgentProfile, result.profile)
        assertEquals(agentRating, result.rating)
        assertEquals(agentImages.first(), result.avatar)
        assertEquals(2, result.images.size)
        verify(agentProfileRepository).findById(testAgentId)
        verify(agentRatingRepository).findByAgentId(testAgentId)
        verify(agentImageRepository).getAgentImages(testAgentId)
    }

    @Test
    fun `getAgent should throw exception when agent not found`() {
        whenever(agentProfileRepository.findById(testAgentId))
            .thenReturn(Optional.empty())

        val exception = assertThrows<RuntimeException> {
            agentProfileService.getAgent(testAgentId)
        }
        assertEquals("Agent(id=$testAgentId) does not exist", exception.message)
        verify(agentProfileRepository).findById(testAgentId)
        verifyNoInteractions(agentRatingRepository, agentImageRepository)
    }

    @Test
    fun `getBriefAgent should return brief info when agent exists`() {
        val avatar = AgentImage(
            UUID.randomUUID(),
            testAgentId,
            "avatar.jpg",
            LocalDateTime.now(),
            true
        )
        whenever(agentProfileRepository.findById(testAgentId))
            .thenReturn(Optional.of(testAgentProfile))
        whenever(agentImageRepository.getAgentAvatar(testAgentId))
            .thenReturn(avatar)

        val result = agentProfileService.getBriefAgent(testAgentId)

        assertEquals(testAgentId, result.agentId)
        assertEquals(testAgentProfile.name, result.name)
        assertEquals(avatar, result.avatar)
        verify(agentProfileRepository).findById(testAgentId)
        verify(agentImageRepository).getAgentAvatar(testAgentId)
    }

    @Test
    fun `getBriefAgent should throw exception when agent not found`() {
        whenever(agentProfileRepository.findById(testAgentId)).thenReturn(Optional.empty())
        val exception = assertThrows<RuntimeException> {
            agentProfileService.getBriefAgent(testAgentId)
        }
        assertEquals("Agent(id=$testAgentId) does not exist", exception.message)
        verify(agentProfileRepository).findById(testAgentId)
        verifyNoInteractions(agentImageRepository)
    }

    @Test
    fun `getAgents should return paginated list of agents sorted by recommendation score`() {
        val agentId1 = UUID.randomUUID()
        val agentId2 = UUID.randomUUID()
        val pageNumber = 0
        val pageSize = 10
        val pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("recommendationScore"))

        val agentRating1 = AgentRating(
            UUID.randomUUID(),
            agentId1,
            gradesSum = 34,
            reviewsNumber = 8,
            BigDecimal("4.5")
        )
        val agentRating2 = AgentRating(
            UUID.randomUUID(),
            agentId2,
            gradesSum = 30,
            reviewsNumber = 7,
            BigDecimal("4.0")
        )
        val ratingsPage = PageImpl(listOf(agentRating1, agentRating2), pageRequest, 2)

        val agentProfile1 = AgentProfile(agentRating1.agentId, "Agent 1", "Description 1")
        val agentProfile2 = AgentProfile(agentRating2.agentId, "Agent 2", "Description 2")

        val agentImage1 = AgentImage(
            UUID.randomUUID(),
            agentRating1.agentId,
            "avatar1.jpg",
            LocalDateTime.now(),
            true
        )
        val agentImage2 = AgentImage(
            UUID.randomUUID(),
            agentRating2.agentId,
            "avatar2.jpg",
            LocalDateTime.now(),
            true
        )

        whenever(agentRatingRepository.findAll(pageRequest))
            .thenReturn(ratingsPage)
        whenever(
            agentProfileRepository.findAllByIdIn(
                listOf(agentRating1.agentId, agentRating2.agentId)
            )
        )
            .thenReturn(listOf(agentProfile1, agentProfile2))
        whenever(
            agentImageRepository.getImagesForAgents(
                listOf(agentRating1.agentId, agentRating2.agentId)
            )
        )
            .thenReturn(listOf(agentImage1, agentImage2))

        val result = agentProfileService.getAgents(pageNumber, pageSize)

        assertEquals(2, result.content.size)
        assertEquals(agentProfile1, result.content[0].profile)
        assertEquals(agentRating1, result.content[0].rating)
        assertEquals(agentImage1, result.content[0].avatar)

        assertEquals(agentProfile2, result.content[1].profile)
        assertEquals(agentRating2, result.content[1].rating)
        assertEquals(agentImage2, result.content[1].avatar)

        verify(agentRatingRepository).findAll(pageRequest)
        verify(agentProfileRepository).findAllByIdIn(listOf(agentRating1.agentId, agentRating2.agentId))
        verify(agentImageRepository).getImagesForAgents(listOf(agentRating1.agentId, agentRating2.agentId))
    }
}