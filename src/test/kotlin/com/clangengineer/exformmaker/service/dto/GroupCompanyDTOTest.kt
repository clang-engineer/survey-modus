package com.clangengineer.exformmaker.service.dto

import com.clangengineer.exformmaker.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class GroupCompanyDTOTest {

    @Test
    fun dtoEqualsVerifier() {
        equalsVerifier(GroupCompanyDTO::class)
        val groupCompanyDTO1 = GroupCompanyDTO()
        groupCompanyDTO1.id = 1L
        val groupCompanyDTO2 = GroupCompanyDTO()
        assertThat(groupCompanyDTO1).isNotEqualTo(groupCompanyDTO2)
        groupCompanyDTO2.id = groupCompanyDTO1.id
        assertThat(groupCompanyDTO1).isEqualTo(groupCompanyDTO2)
        groupCompanyDTO2.id = 2L
        assertThat(groupCompanyDTO1).isNotEqualTo(groupCompanyDTO2)
        groupCompanyDTO1.id = null
        assertThat(groupCompanyDTO1).isNotEqualTo(groupCompanyDTO2)
    }
}
