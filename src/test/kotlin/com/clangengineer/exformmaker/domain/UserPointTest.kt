package com.clangengineer.exformmaker.domain

import com.clangengineer.exformmaker.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class UserPointTest {

  @Test
  fun equalsVerifier() {
    equalsVerifier(UserPoint::class)
    val userPoint1 = UserPoint()
    userPoint1.id = 1L
    val userPoint2 = UserPoint()
    userPoint2.id = userPoint1.id
    assertThat(userPoint1).isEqualTo(userPoint2)
    userPoint2.id = 2L
    assertThat(userPoint1).isNotEqualTo(userPoint2)
    userPoint1.id = null
    assertThat(userPoint1).isNotEqualTo(userPoint2)
  }
}
