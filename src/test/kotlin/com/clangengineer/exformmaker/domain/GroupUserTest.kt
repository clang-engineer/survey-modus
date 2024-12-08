package com.clangengineer.exformmaker.domain

import com.clangengineer.exformmaker.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class GroupUserTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(GroupUser::class)
        val groupUser1 = GroupUser()
        groupUser1.id = 1L
        val groupUser2 = GroupUser()
        groupUser2.id = groupUser1.id
        assertThat(groupUser1).isEqualTo(groupUser2)
        groupUser2.id = 2L
        assertThat(groupUser1).isNotEqualTo(groupUser2)
        groupUser1.id = null
        assertThat(groupUser1).isNotEqualTo(groupUser2)
    }
}
