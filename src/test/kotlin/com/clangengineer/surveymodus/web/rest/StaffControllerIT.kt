package com.clangengineer.surveymodus.web.rest

import com.clangengineer.surveymodus.IntegrationTest
import com.clangengineer.surveymodus.domain.embeddable.Staff
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@IntegrationTest
@AutoConfigureMockMvc
class StaffControllerIT {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var em: EntityManager

    @Test
    @Transactional
    fun `test exists staff issue otp`() {
        val company = CompanyResourceIT.createEntity(em)
        company.staffs = mutableSetOf(
            Staff(phone = "1234567890", activated = true)
        )

        em.persist(company)
        em.flush()

        mockMvc.perform(post("/api/staff/issue-otp").content("1234567890")
            .contentType("text/plain"))
            .andExpect(status().isOk)
    }

    @Test
    @Transactional
    fun `test not exists staff issue otp`() {
        mockMvc.perform(post("/api/staff/issue-otp").content("1234567890")
            .contentType("text/plain"))
            .andExpect(status().isBadRequest)
    }

    @Test
    @Transactional
    fun `test authenticate`() {
        val login = StaffController.StaffLoginVM(phone = "1234567890", otp = "123456")
        mockMvc.perform(
            post("/api/staff/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(login))
        ).andExpect(status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.id_token").isString)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.id_token").isNotEmpty)
            .andExpect(MockMvcResultMatchers.header().string("Authorization", Matchers.not(Matchers.nullValue())))
            .andExpect(MockMvcResultMatchers.header().string("Authorization", Matchers.not(Matchers.`is`(Matchers.emptyString()))))
    }
}
