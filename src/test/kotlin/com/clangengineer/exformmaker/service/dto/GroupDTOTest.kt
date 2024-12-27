package com.clangengineer.surveymodus.service.dto

import com.clangengineer.surveymodus.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class GroupDTOTest {

    @Test
    fun dtoEqualsVerifier() {
        equalsVerifier(GroupDTO::class)
        val groupDTO1 = GroupDTO()
        groupDTO1.id = 1L
        val groupDTO2 = GroupDTO()
        assertThat(groupDTO1).isNotEqualTo(groupDTO2)
        groupDTO2.id = groupDTO1.id
        assertThat(groupDTO1).isEqualTo(groupDTO2)
        groupDTO2.id = 2L
        assertThat(groupDTO1).isNotEqualTo(groupDTO2)
        groupDTO1.id = null
        assertThat(groupDTO1).isNotEqualTo(groupDTO2)
    }
}
