package com.clangengineer.exformmaker.service.dto

import com.clangengineer.exformmaker.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CompanyFormDTOTest {

    @Test
    fun dtoEqualsVerifier() {
        equalsVerifier(CompanyFormDTO::class)
        val companyFormDTO1 = CompanyFormDTO()
        companyFormDTO1.id = 1L
        val companyFormDTO2 = CompanyFormDTO()
        assertThat(companyFormDTO1).isNotEqualTo(companyFormDTO2)
        companyFormDTO2.id = companyFormDTO1.id
        assertThat(companyFormDTO1).isEqualTo(companyFormDTO2)
        companyFormDTO2.id = 2L
        assertThat(companyFormDTO1).isNotEqualTo(companyFormDTO2)
        companyFormDTO1.id = null
        assertThat(companyFormDTO1).isNotEqualTo(companyFormDTO2)
    }
}
