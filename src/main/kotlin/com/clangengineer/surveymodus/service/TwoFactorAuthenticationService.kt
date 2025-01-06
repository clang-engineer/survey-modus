package com.clangengineer.surveymodus.service

interface TwoFactorAuthenticationService {
    fun issueCode(key: String)

    fun verifyCode(key: String, code: String): Boolean
}
