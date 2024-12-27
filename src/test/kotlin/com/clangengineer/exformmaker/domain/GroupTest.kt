package com.clangengineer.surveymodus.domain

import com.clangengineer.surveymodus.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class GroupTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(Group::class)
        val group1 = Group()
        group1.id = 1L
        val group2 = Group()
        group2.id = group1.id
        assertThat(group1).isEqualTo(group2)
        group2.id = 2L
        assertThat(group1).isNotEqualTo(group2)
        group1.id = null
        assertThat(group1).isNotEqualTo(group2)
    }
}
