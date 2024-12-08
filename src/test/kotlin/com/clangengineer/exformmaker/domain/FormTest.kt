package com.clangengineer.exformmaker.domain

import com.clangengineer.exformmaker.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class FormTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(Form::class)
        val form1 = Form()
        form1.id = 1L
        val form2 = Form()
        form2.id = form1.id
        assertThat(form1).isEqualTo(form2)
        form2.id = 2L
        assertThat(form1).isNotEqualTo(form2)
        form1.id = null
        assertThat(form1).isNotEqualTo(form2)
    }
}
