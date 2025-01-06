package com.clangengineer.surveymodus.service

interface TwoFactorAuthenticationService {
    fun issueCode(key: String)

    fun verifyCode(key: String, code: String): Boolean

    fun transformToHash(key: String): String {
        return java.security.MessageDigest.getInstance("SHA-256")
            .digest(key.toByteArray())
            .joinToString("") { "%02x".format(it) }
    }
}
