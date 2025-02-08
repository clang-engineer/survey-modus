package com.clangengineer.surveymodus.web.rest

import com.clangengineer.surveymodus.IntegrationTest
import com.clangengineer.surveymodus.domain.embeddable.Staff
import com.clangengineer.surveymodus.repository.CompanyRepository
import com.clangengineer.surveymodus.security.STAFF
import com.clangengineer.surveymodus.security.USER
import com.clangengineer.surveymodus.service.mapper.CompanyMapper
import org.assertj.core.api.Assertions.*
import org.hamcrest.Matchers.*
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
    @WithMockUser
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

    @Test
    @Transactional
    @Throws(Exception::class)
    @WithMockUser(phone, authorities = [STAFF])
    fun `fetch authorized companies of staff`() {
        val company = CompanyResourceIT.createEntity(em)

        var mockStaff = Staff(
            firstName = "test_firstName",
            lastName = "test_lastName",
            email = "test_email",
            activated = true,
            langKey = "ko",
            phone = phone
        )
        val staffs = company.staffs
        staffs.add(mockStaff)
        company.staffs = staffs

        em.persist(company)
        em.flush()

        mockMvc.perform(
            get("/api/companys/authorized")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("\$.[*].id").value(hasItem(company.id!!.toInt())))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    @WithMockUser("ownlogin", authorities = [USER])
    fun `fetch companies of own`() {
        val user = UserResourceIT.createEntity(em)
        user.login = "ownlogin"
        em.persist(user)

        val company = CompanyResourceIT.createEntity(em)
        company.user = user
        em.persist(company)

        em.flush()

        mockMvc.perform(get("/api/companys/authorized").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("\$.[*].id").value(hasItem(company.id!!.toInt())))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    @WithMockUser("grouplogin", authorities = [USER])
    fun `fetch companies of group`() {
        val group = GroupResourceIT.createEntity(em)
        val company = CompanyResourceIT.createEntity(em)
        val user = UserResourceIT.createEntity(em)
        user.login = "grouplogin"

        em.persist(company)
        em.persist(user)
        em.flush()

        group.companies = mutableSetOf(company)
        group.users = mutableSetOf(user)

        em.persist(group)
        em.flush()

        mockMvc.perform(get("/api/companys/authorized").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("\$.[*].id").value(hasItem(company.id!!.toInt())))
    }

    companion object {
        const val phone = "1234567890"
    }
}
