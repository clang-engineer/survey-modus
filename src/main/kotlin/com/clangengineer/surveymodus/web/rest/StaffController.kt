package com.clangengineer.surveymodus.web.rest

import com.clangengineer.surveymodus.service.StaffService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class StaffController(
    private val staffService: StaffService
) {
    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val ENTITY_NAME = "staff"
    }

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    @PostMapping("/staff/issue-otp")
    fun issueOtp(@RequestBody phone: String): ResponseEntity<Void> {
        log.debug("REST request to issue OTP for Staff : $phone")

        return if (staffService.checkStaffExist(phone)) {
            // todo: issue OTP
            ResponseEntity.ok().build()
        } else {
            ResponseEntity.badRequest().build()
        }
    }
}
