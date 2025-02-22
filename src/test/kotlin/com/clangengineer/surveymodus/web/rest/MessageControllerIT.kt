package com.clangengineer.surveymodus.web.rest

import com.clangengineer.surveymodus.IntegrationTest
import com.clangengineer.surveymodus.service.dto.MessageDTO
import com.clangengineer.surveymodus.web.rest.MessageController.Companion.OBJECT_NAME
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional

@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MessageControllerIT {
    @Autowired
    private lateinit var restMessageMockMvc: MockMvc

    @Autowired
    private lateinit var mongoTemplate: MongoTemplate

    @Test
    @Transactional
    @Throws(Exception::class)
    fun `test create message`() {
        val message = MessageDTO(
            content = "message",
            companyId = 1L
        )

        restMessageMockMvc.perform(
            post("/api/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(message))
        ).andExpect(status().isCreated)

        val messageList =
            mongoTemplate.findAll(MessageDTO::class.java, OBJECT_NAME)

        assertThat(messageList).hasSize(1)
        val testMessage = messageList[0]
        assertThat(testMessage.content).isEqualTo("message")
        assertThat(testMessage.companyId).isEqualTo(1L)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun `test get all messages`() {
        val message = MessageDTO(
            content = "message",
            companyId = 1L
        )

        mongoTemplate.save(message, OBJECT_NAME)
        restMessageMockMvc.perform(
            get("/api/messages")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("\$").isArray)
            .andExpect(jsonPath("\$[0].content").value("message"))
            .andExpect(jsonPath("\$[0].companyId").value(1))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun `delete message`() {
        val message = mongoTemplate.save(
            MessageDTO(
                content = "message",
                companyId = 1L
            ),
            OBJECT_NAME
        )
        restMessageMockMvc.perform(
            delete("/api/messages/${message.id}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        val messageList =
            mongoTemplate.findAll(MessageDTO::class.java, OBJECT_NAME)

        assertThat(messageList).isEmpty()
    }
}
