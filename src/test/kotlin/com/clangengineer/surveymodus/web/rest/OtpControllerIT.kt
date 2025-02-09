package com.clangengineer.surveymodus.web.rest

import com.clangengineer.surveymodus.IntegrationTest
import com.clangengineer.surveymodus.domain.embeddable.Staff
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@IntegrationTest
@AutoConfigureMockMvc
class OtpControllerIT {
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

        mockMvc.perform(
            post("/api/otp/staff").content("1234567890")
                .contentType("text/plain")
        )
            .andExpect(status().isOk)
    }

    @Test
    @Transactional
    fun `test not exists staff issue otp`() {
        mockMvc.perform(
            post("/api/otp/staff").content("1234567890")
                .contentType("text/plain")
        )
            .andExpect(status().isBadRequest)
    }
}
