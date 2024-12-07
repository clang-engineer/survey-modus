package com.clangengineer.exformmaker.service.dto

import com.clangengineer.exformmaker.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class UserCompanyDTOTest {

    @Test
    fun dtoEqualsVerifier() {
        equalsVerifier(UserCompanyDTO::class)
        val userCompanyDTO1 = UserCompanyDTO()
        userCompanyDTO1.id = 1L
        val userCompanyDTO2 = UserCompanyDTO()
        assertThat(userCompanyDTO1).isNotEqualTo(userCompanyDTO2)
        userCompanyDTO2.id = userCompanyDTO1.id
        assertThat(userCompanyDTO1).isEqualTo(userCompanyDTO2)
        userCompanyDTO2.id = 2L
        assertThat(userCompanyDTO1).isNotEqualTo(userCompanyDTO2)
        userCompanyDTO1.id = null
        assertThat(userCompanyDTO1).isNotEqualTo(userCompanyDTO2)
    }
}
