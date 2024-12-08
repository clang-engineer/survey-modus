package com.clangengineer.exformmaker.domain

import com.clangengineer.exformmaker.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CompanyFormTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(CompanyForm::class)
        val companyForm1 = CompanyForm()
        companyForm1.id = 1L
        val companyForm2 = CompanyForm()
        companyForm2.id = companyForm1.id
        assertThat(companyForm1).isEqualTo(companyForm2)
        companyForm2.id = 2L
        assertThat(companyForm1).isNotEqualTo(companyForm2)
        companyForm1.id = null
        assertThat(companyForm1).isNotEqualTo(companyForm2)
    }
}
