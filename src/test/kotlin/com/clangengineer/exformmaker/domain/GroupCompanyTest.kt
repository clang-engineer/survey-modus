package com.clangengineer.exformmaker.domain

import com.clangengineer.exformmaker.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class GroupCompanyTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(GroupCompany::class)
        val groupCompany1 = GroupCompany()
        groupCompany1.id = 1L
        val groupCompany2 = GroupCompany()
        groupCompany2.id = groupCompany1.id
        assertThat(groupCompany1).isEqualTo(groupCompany2)
        groupCompany2.id = 2L
        assertThat(groupCompany1).isNotEqualTo(groupCompany2)
        groupCompany1.id = null
        assertThat(groupCompany1).isNotEqualTo(groupCompany2)
    }
}
