package com.clangengineer.surveymodus.service.impl

import com.clangengineer.surveymodus.service.TwoFactorAuthenticationService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class RedisBaseTwoFactorAuthenticationService(
    private val redisTemplate: org.springframework.data.redis.core.StringRedisTemplate
) : TwoFactorAuthenticationService {
    val logger = org.slf4j.LoggerFactory.getLogger(RedisBaseTwoFactorAuthenticationService::class.java)

    override fun issueCode(key: String) {
        logger.debug("Issue code for key: $key")
        redisTemplate.opsForValue().set(key, "1234", 5, java.util.concurrent.TimeUnit.MINUTES) // 코드 만료 시간 5분 설정
    }

    override fun verifyCode(key: String, code: String): Boolean {
        logger.debug("Verify code for key: $key, code: $code")

        val storedCode = redisTemplate.opsForValue().get(key)
        return storedCode == code
    }
}
