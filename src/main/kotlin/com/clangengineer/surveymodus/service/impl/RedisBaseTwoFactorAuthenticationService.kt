package com.clangengineer.surveymodus.service.impl

import com.clangengineer.surveymodus.service.TwoFactorAuthenticationService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class RedisBaseTwoFactorAuthenticationService(
    private val redisTemplate: org.springframework.data.redis.core.StringRedisTemplate
) : TwoFactorAuthenticationService {
    private final val logger = org.slf4j.LoggerFactory.getLogger(RedisBaseTwoFactorAuthenticationService::class.java)

    override fun issueCode(key: String) {
        logger.debug("Issue code for key: $key")
        // 6자리 코드 생성
        val code = (100000..999999).random().toString()

        redisTemplate.opsForValue().set(key, code, 5, java.util.concurrent.TimeUnit.MINUTES) // 코드 만료 시간 5분 설정

        // TODO: Send code to user
    }

    override fun verifyCode(key: String, code: String): Boolean {
        logger.debug("Verify code for key: $key, code: $code")

        val storedCode = redisTemplate.opsForValue().get(key)
        return storedCode == code
    }
}
