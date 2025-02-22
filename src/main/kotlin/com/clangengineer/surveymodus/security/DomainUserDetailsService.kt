package com.clangengineer.surveymodus.security

import com.clangengineer.surveymodus.domain.User
import com.clangengineer.surveymodus.repository.UserRepository
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator
import org.slf4j.LoggerFactory
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
class DomainUserDetailsService(private val userRepository: UserRepository) : UserDetailsService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun loadUserByUsername(login: String): UserDetails {
        log.debug("Authenticating $login")

        if (EmailValidator().isValid(login, null)) {
            return userRepository.findOneWithAuthoritiesByEmailIgnoreCase(login)
                .map { createSpringSecurityUser(login, it) }
                .orElseThrow { UsernameNotFoundException("User with email $login was not found in the database") }
        }

        val lowercaseLogin = login.lowercase(Locale.ENGLISH)
        return userRepository.findOneWithAuthoritiesByLogin(lowercaseLogin)
            .map { createSpringSecurityUser(lowercaseLogin, it) }
            .orElseThrow { UsernameNotFoundException("User $lowercaseLogin was not found in the database") }
    }

    private fun createSpringSecurityUser(lowercaseLogin: String, user: User): org.springframework.security.core.userdetails.User {
        if (user.activated == null || user.activated == false) {
            throw UserNotActivatedException("User $lowercaseLogin was not activated")
        }
        val grantedAuthorities = user.authorities.map { SimpleGrantedAuthority(it.name) }
        return org.springframework.security.core.userdetails.User(
            user.login!!,
            user.password!!,
            grantedAuthorities
        )
    }
}
