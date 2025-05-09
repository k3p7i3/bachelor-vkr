package ru.hse.fcs.agent.service.interfaces.agent

import org.springframework.core.convert.converter.Converter
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import ru.hse.fcs.agent.service.application.AgentImageService
import ru.hse.fcs.agent.service.application.AgentProfileService
import ru.hse.fcs.agent.service.domain.model.Agent
import ru.hse.fcs.agent.service.domain.model.AgentImage
import ru.hse.fcs.agent.service.domain.model.AgentProfile
import ru.hse.fcs.agent.service.interfaces.agent.dto.*
import java.util.UUID

@RestController
@RequestMapping("/api/agent")
class AgentController(
    private val agentProfileService: AgentProfileService,
    private val agentImageService: AgentImageService,
    private val agentProfileConverter: Converter<AgentProfileDto, AgentProfile>,
    private val agentProfileToDtoConverter: Converter<AgentProfile, AgentProfileDto>,
    private val agentConverter: Converter<Agent, AgentDto>,
    private val imageConverter: Converter<AgentImage, AgentImageDto>
) {

    @PostMapping("/create")
    fun createAgent(@RequestBody body: CreateAgentRequest): ResponseEntity<CreateAgentResponse> {
        if (body.agentProfile.name == null) {
            body.agentProfile.name = body.userData.firstName + " " + body.userData.lastName
        }
        val agentProfile = agentProfileConverter.convert(body.agentProfile)!!

        val result = agentProfileService.createAgent(
            agentProfile, body.userData
        )
        return ResponseEntity.ok(
            CreateAgentResponse(
                agentConverter.convert(result.first)!!,
                result.second
            )
        )
    }

    @PutMapping("/update")
    fun updateAgentProfile(@RequestBody body: AgentProfileDto): ResponseEntity<AgentProfileDto> {
        val profile = agentProfileConverter.convert(body)!!
        val updatedProfile = agentProfileService.updateAgentProfile(profile)
        return ResponseEntity.ok(
            agentProfileToDtoConverter.convert(updatedProfile)
        )
    }

    @GetMapping
    fun getAgent(@RequestParam id: UUID): ResponseEntity<AgentDto> {
        val agent = agentProfileService.getAgent(id)
        return ResponseEntity.ok(
            agentConverter.convert(agent)
        )
    }

    @GetMapping("/brief")
    fun getAgentBrief(@RequestParam id: UUID): ResponseEntity<AgentBriefDto> {
        val agent = agentProfileService.getBriefAgent(id)
        return ResponseEntity.ok(
            AgentBriefDto(
                agent.agentId,
                agent.name,
                avatar = agent.avatar?.let { imageConverter.convert(it) }
            )
        )
    }

    @GetMapping("/search")
    fun getAgents(
        @RequestParam pageNumber: Int = 0,
        @RequestParam pageSize: Int = 10
    ): ResponseEntity<Page<AgentDto>> {
        val agents = agentProfileService.getAgents(pageNumber, pageSize)
        return ResponseEntity.ok(
            agents.map { agentConverter.convert(it)!! }
        )
    }

    @PostMapping("/avatar")
    fun addAvatar(
        @RequestParam agentId: UUID,
        @RequestPart file: MultipartFile
    ): ResponseEntity<AgentImageDto> {
        val avatar = agentImageService.addAvatarForAgent(agentId, file)
        return ResponseEntity.ok(
            imageConverter.convert(avatar)!!
        )
    }

    @PostMapping("/images")
    fun addImages(
        @RequestParam agentId: UUID,
        @RequestPart files: Array<MultipartFile>
    ): ResponseEntity<List<AgentImageDto>> {
        val images = agentImageService.addAgentImages(agentId, files)
        return ResponseEntity.ok(
            images.map {
                imageConverter.convert(it)!!
            }
        )
    }

    @DeleteMapping("/image")
    fun deleteImage(
        @RequestParam imageId: UUID
    ): ResponseEntity<String> {
        agentImageService.deleteAgentImage(imageId)
        return ResponseEntity.ok(imageId.toString())
    }
}