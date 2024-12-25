package com.clangengineer.exformmaker.service.mapper

import org.junit.jupiter.api.BeforeEach

class FieldMapperTest {
    private lateinit var fieldMapper: FieldMapper

    @BeforeEach
    fun setUp() {
        fieldMapper = FieldMapperImpl()
    }
}
