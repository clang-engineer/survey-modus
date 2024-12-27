package com.clangengineer.surveymodus.domain

import com.clangengineer.surveymodus.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CompanyTest {
    @Test
    fun equalsVerifier() {
        equalsVerifier(Company::class)
        val company1 = Company()
        company1.id = 1L
        val company2 = Company()
        company2.id = company1.id
        assertThat(company1).isEqualTo(company2)
        company2.id = 2L
        assertThat(company1).isNotEqualTo(company2)
        company1.id = null
        assertThat(company1).isNotEqualTo(company2)
    }
}
