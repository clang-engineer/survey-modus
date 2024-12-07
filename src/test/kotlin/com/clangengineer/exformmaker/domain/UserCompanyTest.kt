package com.clangengineer.exformmaker.domain

import com.clangengineer.exformmaker.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class UserCompanyTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(UserCompany::class)
        val userCompany1 = UserCompany()
        userCompany1.id = 1L
        val userCompany2 = UserCompany()
        userCompany2.id = userCompany1.id
        assertThat(userCompany1).isEqualTo(userCompany2)
        userCompany2.id = 2L
        assertThat(userCompany1).isNotEqualTo(userCompany2)
        userCompany1.id = null
        assertThat(userCompany1).isNotEqualTo(userCompany2)
    }
}
