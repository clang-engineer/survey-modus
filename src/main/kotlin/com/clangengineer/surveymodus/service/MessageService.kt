package com.clangengineer.surveymodus.service

import com.clangengineer.surveymodus.config.MONGO_OBJECT_ID
import com.clangengineer.surveymodus.service.dto.MessageDTO
import com.clangengineer.surveymodus.web.rest.MessageController.Companion.OBJECT_NAME
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
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

        return mongoTemplate.save(message, OBJECT_NAME) as MessageDTO
    }

    fun delete(id: String) {
        log.debug("Delete message: $id")

        val query = Query.query(Criteria.where(MONGO_OBJECT_ID).`is`(ObjectId(id)))

        mongoTemplate.remove(query, OBJECT_NAME)
    }
}
