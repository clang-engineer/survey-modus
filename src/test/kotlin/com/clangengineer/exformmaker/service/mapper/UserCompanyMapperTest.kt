package com.clangengineer.exformmaker.service.mapper

import org.junit.jupiter.api.BeforeEach

class UserCompanyMapperTest {

    private lateinit var userCompanyMapper: UserCompanyMapper

    @BeforeEach
    fun setUp() {
        userCompanyMapper = UserCompanyMapperImpl()
    }
}
