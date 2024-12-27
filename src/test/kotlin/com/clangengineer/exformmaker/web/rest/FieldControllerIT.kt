package com.clangengineer.exformmaker.web.rest

import com.clangengineer.surveymodus.IntegrationTest
import com.clangengineer.surveymodus.repository.FieldRepository
import com.clangengineer.surveymodus.service.mapper.FieldMapper
import com.clangengineer.surveymodus.web.rest.FieldResourceIT
import com.clangengineer.surveymodus.web.rest.convertObjectToJsonBytes
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FieldControllerIT {
    @Autowired
    private lateinit var fieldRepository: FieldRepository

    @Autowired
    private lateinit var fieldMapper: FieldMapper

    @Autowired
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createAndUpdateAllFieldsTest() {
        val field1 = FieldResourceIT.createEntity(em)
        em.persist(field1)
        em.flush()

        val field2 = FieldResourceIT.createEntity(em)

        val fieldsDTO = listOf(field1, field2).map { fieldMapper.toDto(it) }

        val databaseSizeBeforeBulkInsert = fieldRepository.findAll().size

        mockMvc.perform(
            put("/api/fields/bulk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(fieldsDTO))
        ).andExpect(status().isOk)

        val fieldList = fieldRepository.findAll()
        assertThat(fieldList).hasSize(databaseSizeBeforeBulkInsert + 1)

        val updated = fieldRepository.findById(field1.id!!).get()
        assertThat(updated).isEqualTo(field1)
    }
}
