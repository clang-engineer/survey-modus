package com.clangengineer.surveymodus.web.rest

import com.clangengineer.surveymodus.service.MessageService
import com.clangengineer.surveymodus.service.dto.MessageDTO
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/api")
class MessageController(
    private val messageService: MessageService
) {
    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val OBJECT_NAME = "message"
    }

    @PostMapping("/messages")
    fun createMessage(@RequestBody message: MessageDTO): ResponseEntity<MessageDTO> {
        log.debug("REST request to save Message")

        val result = messageService.saveMessage(message)

        return ResponseEntity.created(
            URI("/api/messages/${result.id}")
        ).body(result)
    }
}
