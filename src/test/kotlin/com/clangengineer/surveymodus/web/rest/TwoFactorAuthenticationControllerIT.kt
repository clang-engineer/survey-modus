package com.clangengineer.surveymodus.web.rest

import com.clangengineer.surveymodus.IntegrationTest
import com.clangengineer.surveymodus.service.TwoFactorAuthenticationService
import com.clangengineer.surveymodus.service.dto.TwoFactorAuthenticationDTO
import com.clangengineer.surveymodus.web.rest.errors.ERR_VALIDATION
import org.assertj.core.api.Assertions.*
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
        verificationCodes[key] = Math.random().toString().substring(2, 8)
    }

    override fun verifyCode(key: String, code: String): Boolean {
        return verificationCodes[key] == code
    }

    fun getVerificationCodes(): Map<String, String> {
        return verificationCodes
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
    fun `test staff verification code issue and verify`() {
        val twoFactorAuthenticationDTO =
            TwoFactorAuthenticationDTO(namespace = "staffs", phoneNumber = "010-1234-5678")

        mockMvc.perform(
            post("/api/two-factor-authentications/issue")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(twoFactorAuthenticationDTO))
        ).andExpect(status().isOk)

        val verificationCodes =
            (twoFactorAuthenticationService as MemoryBasedTwoFactorAuthenticationService).getVerificationCodes()
        val key = verificationCodes[twoFactorAuthenticationDTO.toKey()]
        assertThat(key).isNotEmpty
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun `test staff verification code phone number format validation`() {
        val twoFactorAuthenticationDTO = TwoFactorAuthenticationDTO(namespace = "staffs", phoneNumber = "01012345678")

        mockMvc.perform(
            post("/api/two-factor-authentications/issue")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(twoFactorAuthenticationDTO))
        ).andExpect(status().isBadRequest)
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
            .andExpect(jsonPath("\$.message").value(ERR_VALIDATION))
            .andExpect(jsonPath("\$.fieldErrors.[0].objectName").value("twoFactorAuthentication"))
            .andExpect(jsonPath("\$.fieldErrors.[0].field").value("phoneNumber"))
            .andExpect(jsonPath("\$.fieldErrors.[0].message").value("전화번호는 01x-XXX(XX)-XXXX 형식이어야 합니다."))
    }
}
