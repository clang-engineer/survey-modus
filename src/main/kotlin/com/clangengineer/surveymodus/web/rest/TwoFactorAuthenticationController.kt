package com.clangengineer.surveymodus.web.rest

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@RestController
@RequestMapping("/api")
class TwoFactorAuthenticationController {

    private val logger = LoggerFactory.getLogger(TwoFactorAuthenticationController::class.java)

    data class StaffVerificationCodeVM(
        @field:NotBlank
        val company: String = "company",

        @field:NotBlank
        val phoneNumber: String = "010-1234-5678"
    )

    @PostMapping("/two-factor-authentication/staff")
    fun issueStaffVerificationCode(
        @RequestBody @Valid staffVerificationCodeVM: StaffVerificationCodeVM
    ): ResponseEntity<Void> {
        logger.debug("REST request to issue staff verification code")

        return ResponseEntity.ok().build()
    }
}
