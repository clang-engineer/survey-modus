package com.clangengineer.surveymodus.service.dto

import com.clangengineer.surveymodus.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class FormDTOTest {

    @Test
    fun dtoEqualsVerifier() {
        equalsVerifier(FormDTO::class)
        val formDTO1 = FormDTO()
        formDTO1.id = 1L
        val formDTO2 = FormDTO()
        assertThat(formDTO1).isNotEqualTo(formDTO2)
        formDTO2.id = formDTO1.id
        assertThat(formDTO1).isEqualTo(formDTO2)
        formDTO2.id = 2L
        assertThat(formDTO1).isNotEqualTo(formDTO2)
        formDTO1.id = null
        assertThat(formDTO1).isNotEqualTo(formDTO2)
    }
}
