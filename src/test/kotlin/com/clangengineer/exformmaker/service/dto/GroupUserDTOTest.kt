package com.clangengineer.exformmaker.service.dto

import com.clangengineer.exformmaker.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class GroupUserDTOTest {

    @Test
    fun dtoEqualsVerifier() {
        equalsVerifier(GroupUserDTO::class)
        val groupUserDTO1 = GroupUserDTO()
        groupUserDTO1.id = 1L
        val groupUserDTO2 = GroupUserDTO()
        assertThat(groupUserDTO1).isNotEqualTo(groupUserDTO2)
        groupUserDTO2.id = groupUserDTO1.id
        assertThat(groupUserDTO1).isEqualTo(groupUserDTO2)
        groupUserDTO2.id = 2L
        assertThat(groupUserDTO1).isNotEqualTo(groupUserDTO2)
        groupUserDTO1.id = null
        assertThat(groupUserDTO1).isNotEqualTo(groupUserDTO2)
    }
}
