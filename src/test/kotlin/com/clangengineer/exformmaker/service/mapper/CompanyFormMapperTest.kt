package com.clangengineer.exformmaker.service.mapper

import org.junit.jupiter.api.BeforeEach

class CompanyFormMapperTest {

    private lateinit var companyFormMapper: CompanyFormMapper

    @BeforeEach
    fun setUp() {
        companyFormMapper = CompanyFormMapperImpl()
    }
}
