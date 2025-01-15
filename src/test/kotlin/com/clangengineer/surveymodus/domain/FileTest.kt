package com.clangengineer.surveymodus.domain

import com.clangengineer.surveymodus.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class FileTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(File::class)
        val file1 = File()
        file1.id = 1L
        val file2 = File()
        file2.id = file1.id
        assertThat(file1).isEqualTo(file2)
        file2.id = 2L
        assertThat(file1).isNotEqualTo(file2)
        file1.id = null
        assertThat(file1).isNotEqualTo(file2)
    }
}
