package ru.hse.fcs.agent.service.application

import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever
import ru.hse.fcs.agent.service.domain.model.AgentBriefInfo
import ru.hse.fcs.agent.service.domain.model.AgentSelection
import ru.hse.fcs.agent.service.domain.repository.AgentProfileRepository
import ru.hse.fcs.agent.service.domain.repository.AgentSelectionRepository
import java.util.Optional
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

@ExtendWith(MockitoExtension::class)
class AgentSelectionServiceTest {

    @Mock
    private lateinit var agentProfileService: AgentProfileService

    @Mock
    private lateinit var agentProfileRepository: AgentProfileRepository

    @Mock
    private lateinit var agentSelectionRepository: AgentSelectionRepository

    @InjectMocks
    private lateinit var agentSelectionService: AgentSelectionService

    private val userId = UUID.randomUUID()
    private val agentId = UUID.randomUUID()
    private val agentSelection = AgentSelection(UUID.randomUUID(), userId, agentId)
    private val agentBriefInfo = AgentBriefInfo(agentId, "Test Agent", null)

    @Test
    fun `saveAgentSelection should throw exception when agent does not exist`() {
        whenever(agentProfileRepository.existsById(agentId))
            .thenReturn(false)

        val exception = assertFailsWith<RuntimeException> {
            agentSelectionService.saveAgentSelection(agentSelection)
        }
        assertEquals("Agent with id=$agentId does not exist", exception.message)
        verify(agentProfileRepository).existsById(agentId)
        verifyNoInteractions(agentSelectionRepository)
    }

    @Test
    fun `saveAgentSelection should save new selection when none exists`() {
        whenever(agentProfileRepository.existsById(agentId))
            .thenReturn(true)
        whenever(agentSelectionRepository.findByUserId(userId))
            .thenReturn(Optional.empty())
        whenever(agentSelectionRepository.save(agentSelection))
            .thenReturn(agentSelection)
        val result = agentSelectionService.saveAgentSelection(agentSelection)
        assertEquals(agentSelection, result)
        verify(agentSelectionRepository).findByUserId(userId)
        verify(agentSelectionRepository).save(agentSelection)
    }

    @Test
    fun `saveAgentSelection should update existing selection`() {
        val existingSelection = AgentSelection(UUID.randomUUID(), userId, UUID.randomUUID())
        whenever(agentProfileRepository.existsById(agentId))
            .thenReturn(true)
        whenever(agentSelectionRepository.findByUserId(userId))
            .thenReturn(Optional.of(existingSelection))
        whenever(agentSelectionRepository.save(any<AgentSelection>()))
            .thenAnswer { it.arguments[0] as AgentSelection }

        val result = agentSelectionService.saveAgentSelection(agentSelection)

        assertEquals(agentId, result.agentId)
        verify(agentSelectionRepository).findByUserId(userId)
        verify(agentSelectionRepository).save(any())
    }

    @Test
    fun `getSelectedAgent should return null when no selection exists`() {
        whenever(agentSelectionRepository.findByUserId(userId)).thenReturn(Optional.empty())
        val result = agentSelectionService.getSelectedAgent(userId)
        assertNull(result)
        verify(agentSelectionRepository).findByUserId(userId)
        verifyNoInteractions(agentProfileService)
    }

    @Test
    fun `getSelectedAgent should return agent brief info when selection exists`() {
        whenever(agentSelectionRepository.findByUserId(userId)).thenReturn(Optional.of(agentSelection))
        whenever(agentProfileService.getBriefAgent(agentId)).thenReturn(agentBriefInfo)
        val result = agentSelectionService.getSelectedAgent(userId)
        assertEquals(agentBriefInfo, result)
        verify(agentSelectionRepository).findByUserId(userId)
        verify(agentProfileService).getBriefAgent(agentId)
    }

    @Test
    fun `deleteAgentSelection should delete by user id`() {
        agentSelectionService.deleteAgentSelection(userId)
        verify(agentSelectionRepository).deleteByUserId(userId)
    }
}