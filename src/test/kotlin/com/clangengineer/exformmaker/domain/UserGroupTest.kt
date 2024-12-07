package com.clangengineer.exformmaker.domain

import com.clangengineer.exformmaker.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class UserGroupTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(UserGroup::class)
        val userGroup1 = UserGroup()
        userGroup1.id = 1L
        val userGroup2 = UserGroup()
        userGroup2.id = userGroup1.id
        assertThat(userGroup1).isEqualTo(userGroup2)
        userGroup2.id = 2L
        assertThat(userGroup1).isNotEqualTo(userGroup2)
        userGroup1.id = null
        assertThat(userGroup1).isNotEqualTo(userGroup2)
    }
}
