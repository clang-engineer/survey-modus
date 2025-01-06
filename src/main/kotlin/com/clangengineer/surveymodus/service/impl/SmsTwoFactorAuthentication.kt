package com.clangengineer.surveymodus.service.impl

import com.clangengineer.surveymodus.service.TwoFactorAuthenticationService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class SmsTwoFactorAuthentication : TwoFactorAuthenticationService {
    val logger = org.slf4j.LoggerFactory.getLogger(SmsTwoFactorAuthentication::class.java)
    override fun issueCode(key: String) {
        logger.debug("Issue code for key: $key")
    }

    override fun verifyCode(key: String, code: String): Boolean {
        logger.debug("Verify code for key: $key, code: $code")
        return true
    }
}
