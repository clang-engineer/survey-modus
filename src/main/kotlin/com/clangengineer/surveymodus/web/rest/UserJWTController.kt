package com.clangengineer.surveymodus.web.rest

import com.clangengineer.surveymodus.security.STAFF
import com.clangengineer.surveymodus.security.jwt.JWTFilter
import com.clangengineer.surveymodus.security.jwt.TokenProvider
import com.clangengineer.surveymodus.web.rest.vm.LoginVM
import com.clangengineer.surveymodus.web.rest.vm.StaffLoginVM
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
class UserJWTController(
    private val tokenProvider: TokenProvider,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder
) {
    @PostMapping("/authenticate")
    fun authorize(@Valid @RequestBody loginVM: LoginVM): ResponseEntity<JWTToken> {

        val authenticationToken = UsernamePasswordAuthenticationToken(loginVM.username, loginVM.password)

        val authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken)
        SecurityContextHolder.getContext().authentication = authentication
        val jwt = tokenProvider.createToken(authentication, loginVM.isRememberMe ?: false)
        val httpHeaders = HttpHeaders()
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer $jwt")
        return ResponseEntity(JWTToken(jwt), httpHeaders, HttpStatus.OK)
    }

    @PostMapping("/authenticate/staff")
    fun authenticate(@Valid @RequestBody staffLoginVM: StaffLoginVM): ResponseEntity<JWTToken> {
        // 사용자 정보 수동 생성
        val authorities = listOf(SimpleGrantedAuthority(STAFF))
        val customAuthentication = UsernamePasswordAuthenticationToken(staffLoginVM.phone, null, authorities)

        // 수동으로 시큐리티 컨텍스트 설정
        SecurityContextHolder.getContext().authentication = customAuthentication

        // JWT 생성
        val jwt = tokenProvider.createToken(customAuthentication, false)
        val httpHeaders = HttpHeaders()
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer $jwt")
        return ResponseEntity(JWTToken(jwt), httpHeaders, HttpStatus.OK)
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    class JWTToken(@get:JsonProperty("id_token") var idToken: String?)
}
