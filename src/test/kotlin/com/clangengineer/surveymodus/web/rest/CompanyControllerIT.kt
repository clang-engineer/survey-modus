package com.clangengineer.surveymodus.web.rest

import com.clangengineer.surveymodus.IntegrationTest
import com.clangengineer.surveymodus.repository.CompanyRepository
import com.clangengineer.surveymodus.service.mapper.CompanyMapper
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
class CompanyControllerIT {
    @Autowired
    private lateinit var companyRepository: CompanyRepository

    @Autowired
    private lateinit var companyMapper: CompanyMapper

    @Autowired
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createAndUpdateAllCompanysTest() {
        val company1 = CompanyResourceIT.createEntity(em)
        em.persist(company1)
        em.flush()

        val company2 = CompanyResourceIT.createEntity(em)

        val companysDTO = listOf(company1, company2).map { companyMapper.toDto(it) }

        val databaseSizeBeforeBulkInsert = companyRepository.findAll().size

        mockMvc.perform(
            put("/api/companys/all")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(companysDTO))
        ).andExpect(status().isOk)

        val companyList = companyRepository.findAll()
        assertThat(companyList).hasSize(databaseSizeBeforeBulkInsert + 1)

        val updated = companyRepository.findById(company1.id!!).get()
        assertThat(updated).isEqualTo(company1)
    }
}
