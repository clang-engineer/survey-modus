package com.clangengineer.surveymodus.service.impl

import com.clangengineer.surveymodus.IntegrationTest
import com.clangengineer.surveymodus.service.TwoFactorAuthenticationService
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.security.test.context.support.WithMockUser

@IntegrationTest
@WithMockUser
class RedisBaseTwoFactorAuthenticationServiceIT {

    @MockBean
    private lateinit var redisTemplate: StringRedisTemplate

    @Autowired
    private lateinit var twoFactorAuthenticationService: TwoFactorAuthenticationService

    @BeforeEach
    fun setUp() {
        whenever(redisTemplate.opsForValue()).thenReturn(redisTemplate.opsForValue())
    }
    @Test
    fun `issueCode should store code in Redis`() {
        val key = "company:010-1234-5678"

        // issueCode 메서드 실행
        twoFactorAuthenticationService.issueCode(key)

        // Redis에 set이 호출되었는지 확인
        verify(redisTemplate).opsForValue().set(key, "1234", 5, java.util.concurrent.TimeUnit.MINUTES)
    }

    @Test
    fun `verifyCode should return true if code matches`() {
        val key = "company:010-1234-5678"
        val code = "1234"

        // Redis에서 값을 가져오도록 모킹
        whenever(redisTemplate.opsForValue().get(key)).thenReturn("1234")

        // verifyCode 메서드 실행
        val result = twoFactorAuthenticationService.verifyCode(key, code)

        // 결과 검증
        assertTrue(result)
    }

    @Test
    fun `verifyCode should return false if code does not match`() {
        val key = "company:010-1234-5678"
        val code = "4321"

        // Redis에서 값을 가져오도록 모킹
        whenever(redisTemplate.opsForValue().get(key)).thenReturn("1234")

        // verifyCode 메서드 실행
        val result = twoFactorAuthenticationService.verifyCode(key, code)

        // 결과 검증
        assertFalse(result)
    }
}
