package com.clangengineer.surveymodus.service.dto

import com.clangengineer.surveymodus.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CategoryDTOTest {

    @Test
    fun dtoEqualsVerifier() {
        equalsVerifier(CategoryDTO::class)
        val categoryDTO1 = CategoryDTO()
        categoryDTO1.id = 1L
        val categoryDTO2 = CategoryDTO()
        assertThat(categoryDTO1).isNotEqualTo(categoryDTO2)
        categoryDTO2.id = categoryDTO1.id
        assertThat(categoryDTO1).isEqualTo(categoryDTO2)
        categoryDTO2.id = 2L
        assertThat(categoryDTO1).isNotEqualTo(categoryDTO2)
        categoryDTO1.id = null
        assertThat(categoryDTO1).isNotEqualTo(categoryDTO2)
    }
}
