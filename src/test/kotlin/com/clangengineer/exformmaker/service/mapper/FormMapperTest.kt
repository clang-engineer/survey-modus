package com.clangengineer.exformmaker.service.mapper

import org.junit.jupiter.api.BeforeEach

class FormMapperTest {
    private lateinit var formMapper: FormMapper

    @BeforeEach
    fun setUp() {
        formMapper = FormMapperImpl()
    }
}
