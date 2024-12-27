package com.clangengineer.surveymodus.service.dto

import com.clangengineer.surveymodus.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class UserPointDTOTest {

    @Test
    fun dtoEqualsVerifier() {
        equalsVerifier(UserPointDTO::class)
        val userPointDTO1 = UserPointDTO()
        userPointDTO1.id = 1L
        val userPointDTO2 = UserPointDTO()
        assertThat(userPointDTO1).isNotEqualTo(userPointDTO2)
        userPointDTO2.id = userPointDTO1.id
        assertThat(userPointDTO1).isEqualTo(userPointDTO2)
        userPointDTO2.id = 2L
        assertThat(userPointDTO1).isNotEqualTo(userPointDTO2)
        userPointDTO1.id = null
        assertThat(userPointDTO1).isNotEqualTo(userPointDTO2)
    }
}
