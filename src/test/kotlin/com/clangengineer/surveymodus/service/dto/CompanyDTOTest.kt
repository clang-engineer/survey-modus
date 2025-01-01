package com.clangengineer.surveymodus.service.dto

import com.clangengineer.surveymodus.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CompanyDTOTest {
    @Test
    fun dtoEqualsVerifier() {
        equalsVerifier(CompanyDTO::class)
        val companyDTO1 = CompanyDTO()
        companyDTO1.id = 1L
        val companyDTO2 = CompanyDTO()
        assertThat(companyDTO1).isNotEqualTo(companyDTO2)
        companyDTO2.id = companyDTO1.id
        assertThat(companyDTO1).isEqualTo(companyDTO2)
        companyDTO2.id = 2L
        assertThat(companyDTO1).isNotEqualTo(companyDTO2)
        companyDTO1.id = null
        assertThat(companyDTO1).isNotEqualTo(companyDTO2)
    }
}
