package com.clangengineer.surveymodus.security.jwt

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

/**
 * Filters incoming requests and installs a Spring Security principal if a header corresponding to a valid user is
 * found.
 */
class JWTFilter(private val tokenProvider: TokenProvider) : GenericFilterBean() {

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, filterChain: FilterChain) {
        val httpServletRequest = servletRequest as HttpServletRequest
        val jwt = resolveToken(httpServletRequest)
        if (!jwt.isNullOrBlank() && this.tokenProvider.validateToken(jwt)) {
            val authentication = this.tokenProvider.getAuthentication(jwt)
            SecurityContextHolder.getContext().authentication = authentication
        }
        filterChain.doFilter(servletRequest, servletResponse)
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader(AUTHORIZATION_HEADER)
        if (!bearerToken.isNullOrBlank() && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7)
        }
        val jwt = request.getParameter(AUTHORIZATION_TOKEN)
        if (!jwt.isNullOrBlank()) {
            return jwt
        }
        return null
    }

    companion object {
        const val AUTHORIZATION_HEADER = "Authorization"

        const val AUTHORIZATION_TOKEN: String = "access_token"
    }
}
