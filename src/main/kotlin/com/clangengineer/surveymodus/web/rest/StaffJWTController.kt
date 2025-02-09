package com.clangengineer.surveymodus.web.rest

import com.clangengineer.surveymodus.security.STAFF
import com.clangengineer.surveymodus.security.jwt.JWTFilter
import com.clangengineer.surveymodus.security.jwt.TokenProvider
import com.clangengineer.surveymodus.service.StaffService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class StaffController(
    private val tokenProvider: TokenProvider,
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

    @PostMapping("/staff/authenticate")
    fun authenticate(
        @RequestBody staffLoginVM: StaffLoginVM
    ): ResponseEntity<UserJWTController.JWTToken> {
        log.debug("REST request to authenticate Staff : $staffLoginVM")

        // 사용자 정보 수동 생성
        val authorities = listOf(SimpleGrantedAuthority(STAFF))
        val customAuthentication = UsernamePasswordAuthenticationToken(staffLoginVM.phone, null, authorities)

        // 수동으로 시큐리티 컨텍스트 설정
        SecurityContextHolder.getContext().authentication = customAuthentication

        // JWT 생성
        val jwt = tokenProvider.createToken(customAuthentication, false)
        val httpHeaders = HttpHeaders()
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer $jwt")
        return ResponseEntity(UserJWTController.JWTToken(jwt), httpHeaders, HttpStatus.OK)
    }

    class StaffLoginVM(
        var phone: String? = null,
        var otp: String? = null
    )
}
