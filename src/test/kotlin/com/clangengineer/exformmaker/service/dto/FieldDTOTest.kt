package com.clangengineer.exformmaker.service.dto

import com.clangengineer.exformmaker.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class FieldDTOTest {

    @Test
    fun dtoEqualsVerifier() {
        equalsVerifier(FieldDTO::class)
        val fieldDTO1 = FieldDTO()
        fieldDTO1.id = 1L
        val fieldDTO2 = FieldDTO()
        assertThat(fieldDTO1).isNotEqualTo(fieldDTO2)
        fieldDTO2.id = fieldDTO1.id
        assertThat(fieldDTO1).isEqualTo(fieldDTO2)
        fieldDTO2.id = 2L
        assertThat(fieldDTO1).isNotEqualTo(fieldDTO2)
        fieldDTO1.id = null
        assertThat(fieldDTO1).isNotEqualTo(fieldDTO2)
    }
}
