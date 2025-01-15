package com.clangengineer.surveymodus.web.rest

import com.clangengineer.surveymodus.IntegrationTest
import com.clangengineer.surveymodus.repository.FormRepository
import com.clangengineer.surveymodus.service.mapper.FormMapper
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
class FormControllerIT {
    @Autowired
    private lateinit var formRepository: FormRepository

    @Autowired
    private lateinit var formMapper: FormMapper

    @Autowired
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createAndUpdateAllFormsTest() {
        val form1 = FormResourceIT.createEntity(em)
        em.persist(form1)
        em.flush()

        val form2 = FormResourceIT.createEntity(em)

        val formsDTO = listOf(form1, form2).map { formMapper.toDto(it) }

        val databaseSizeBeforeBulkInsert = formRepository.findAll().size

        mockMvc.perform(
            put("/api/forms/all")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(formsDTO))
        ).andExpect(status().isOk)

        val formList = formRepository.findAll()
        assertThat(formList).hasSize(databaseSizeBeforeBulkInsert + 1)

        val updated = formRepository.findById(form1.id!!).get()
        assertThat(updated).isEqualTo(form1)
    }
}
