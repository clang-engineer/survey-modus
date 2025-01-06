package com.clangengineer.surveymodus.web.rest

import com.clangengineer.surveymodus.IntegrationTest
import com.clangengineer.surveymodus.service.TwoFactorAuthenticationService
import com.clangengineer.surveymodus.service.dto.TwoFactorAuthenticationDTO
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional

class MemoryBasedTwoFactorAuthenticationService : TwoFactorAuthenticationService {
    private val verificationCodes = mutableMapOf<String, String>()

    override fun issueCode(key: String) {
        verificationCodes[key] = "1234"
    }

    override fun verifyCode(key: String, code: String): Boolean {
        return verificationCodes[key] == code
    }
}

@TestConfiguration
class TestConfig {
    @Bean
    fun twoFactorAuthenticationService(): TwoFactorAuthenticationService {
        return MemoryBasedTwoFactorAuthenticationService()
    }
}

@Import(TestConfig::class)
@AutoConfigureMockMvc
@IntegrationTest
class TwoFactorAuthenticationControllerIT {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var twoFactorAuthenticationService: TwoFactorAuthenticationService

    @Test
    @Transactional
    @Throws(Exception::class)
    fun testStaffVerificationCodeIssue() {

        val twoFactorAuthenticationDTO = TwoFactorAuthenticationDTO(
            phoneNumber = "010-1234-5678"
        )

        mockMvc.perform(
            post("/api/two-factor-authentication/staff")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(twoFactorAuthenticationDTO))
        ).andExpect(status().isOk)

        assertTrue(twoFactorAuthenticationService.verifyCode(twoFactorAuthenticationDTO.toKey(), "1234"))
    }
}
