package com.clangengineer.surveymodus.domain

import com.clangengineer.surveymodus.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class FieldTest {
    @Test
    fun equalsVerifier() {
        equalsVerifier(Field::class)
        val field1 = Field()
        field1.id = 1L
        val field2 = Field()
        field2.id = field1.id
        assertThat(field1).isEqualTo(field2)
        field2.id = 2L
        assertThat(field1).isNotEqualTo(field2)
        field1.id = null
        assertThat(field1).isNotEqualTo(field2)
    }
}
