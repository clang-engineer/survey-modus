package com.clangengineer.surveymodus.web.rest

import com.clangengineer.surveymodus.service.StaffService
import com.clangengineer.surveymodus.service.dto.StaffDTO
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.jhipster.web.util.ResponseUtil

@RestController
@RequestMapping("/api")
class StaffController(private val staffService: StaffService) {
    private val log = LoggerFactory.getLogger(javaClass)

    @GetMapping("/staff-info")
    fun getStaffInfo(): ResponseEntity<StaffDTO> {
        log.debug("REST request to get staff info")

        val staff = staffService.getStaffSession()

        return ResponseUtil.wrapOrNotFound(staff)
    }
}
