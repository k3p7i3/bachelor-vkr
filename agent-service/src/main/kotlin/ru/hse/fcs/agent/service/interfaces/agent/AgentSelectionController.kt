package ru.hse.fcs.agent.service.interfaces.agent

import org.springframework.core.convert.converter.Converter
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.hse.fcs.agent.service.application.AgentSelectionService
import ru.hse.fcs.agent.service.domain.model.AgentImage
import ru.hse.fcs.agent.service.domain.model.AgentSelection
import ru.hse.fcs.agent.service.interfaces.agent.dto.AgentBriefDto
import ru.hse.fcs.agent.service.interfaces.agent.dto.AgentImageDto
import java.util.*

@RestController
@RequestMapping("/api/agent/selection")
class AgentSelectionController(
    private val agentSelectionService: AgentSelectionService,
    private val imageConverter: Converter<AgentImage, AgentImageDto>
) {
    @PostMapping
    fun saveSelection(@RequestBody body: AgentSelection): ResponseEntity<AgentSelection> {
        val agentSelection = agentSelectionService.saveAgentSelection(body)
        return ResponseEntity.ok(agentSelection)
    }

    @GetMapping
    fun getAgentSelection(@RequestParam userId: UUID): ResponseEntity<AgentBriefDto?> {
        val agent = agentSelectionService.getSelectedAgent(userId)
        return ResponseEntity.ok(
            agent?.let {
                AgentBriefDto(
                    agentId = it.agentId,
                    name = it.name,
                    avatar = it.avatar?.let { img ->
                        imageConverter.convert(img)
                    }
                )
            }
        )
    }

    @DeleteMapping
    fun deleteAgentSelection(@RequestParam userId: UUID) {
        agentSelectionService.deleteAgentSelection(userId)
    }
}