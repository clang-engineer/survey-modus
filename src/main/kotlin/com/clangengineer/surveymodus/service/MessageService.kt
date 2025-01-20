package com.clangengineer.surveymodus.service

import com.clangengineer.surveymodus.service.dto.MessageDTO
import org.slf4j.LoggerFactory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MessageService(
    private val mongoTemplate: MongoTemplate
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun saveMessage(message: MessageDTO): MessageDTO {
        log.debug("Save message: $message")

        return mongoTemplate.save(message)
    }
}
