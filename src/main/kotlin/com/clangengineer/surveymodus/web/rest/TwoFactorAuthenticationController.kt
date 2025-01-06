package com.clangengineer.surveymodus.web.rest

import com.clangengineer.surveymodus.service.TwoFactorAuthenticationService
import com.clangengineer.surveymodus.service.dto.TwoFactorAuthenticationDTO
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api")
class TwoFactorAuthenticationController(val twoFactorAuthenticationService: TwoFactorAuthenticationService) {

    private val logger = LoggerFactory.getLogger(TwoFactorAuthenticationController::class.java)

    @PostMapping("/two-factor-authentication/staff")
    fun issueStaffVerificationCode(
        @RequestBody @Valid twoFactorAuthenticationDTO: TwoFactorAuthenticationDTO
    ): ResponseEntity<Void> {
        logger.debug("REST request to issue staff verification code")

        twoFactorAuthenticationService.issueCode(twoFactorAuthenticationDTO.toKey())

        return ResponseEntity.ok().build()
    }
}
