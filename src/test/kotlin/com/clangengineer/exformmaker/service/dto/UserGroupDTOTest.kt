package com.clangengineer.exformmaker.service.dto

import com.clangengineer.exformmaker.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class UserGroupDTOTest {

    @Test
    fun dtoEqualsVerifier() {
        equalsVerifier(UserGroupDTO::class)
        val userGroupDTO1 = UserGroupDTO()
        userGroupDTO1.id = 1L
        val userGroupDTO2 = UserGroupDTO()
        assertThat(userGroupDTO1).isNotEqualTo(userGroupDTO2)
        userGroupDTO2.id = userGroupDTO1.id
        assertThat(userGroupDTO1).isEqualTo(userGroupDTO2)
        userGroupDTO2.id = 2L
        assertThat(userGroupDTO1).isNotEqualTo(userGroupDTO2)
        userGroupDTO1.id = null
        assertThat(userGroupDTO1).isNotEqualTo(userGroupDTO2)
    }
}
