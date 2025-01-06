package com.clangengineer.surveymodus.web.rest

import com.clangengineer.surveymodus.IntegrationTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional

@AutoConfigureMockMvc
@IntegrationTest
class TwoFactorAuthenticationControllerIT {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    @Transactional
    @Throws(Exception::class)
    fun testStaffVerificationCodeIssue() {
        val verificationCodeVM = TwoFactorAuthenticationController.StaffVerificationCodeVM(
            company = "company",
            phoneNumber = "010-1234-5678"
        )

        mockMvc.perform(
            post("/api/two-factor-authentication/staff")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(verificationCodeVM))
        ).andExpect(status().isOk)
    }
}
