package com.clangengineer.surveymodus.web.rest

import com.clangengineer.surveymodus.IntegrationTest
import com.clangengineer.surveymodus.service.dto.MessageDTO
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
            message = "message",
            companyId = 1L
        )

        restMessageMockMvc.perform(
            post("/api/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(message))
        ).andExpect(status().isCreated)

        val messageList = mongoTemplate.findAll(MessageDTO::class.java)

        assertThat(messageList).hasSize(1)
        val testMessage = messageList[0]
        assertThat(testMessage.message).isEqualTo("message")
        assertThat(testMessage.companyId).isEqualTo(1L)
    }
}
