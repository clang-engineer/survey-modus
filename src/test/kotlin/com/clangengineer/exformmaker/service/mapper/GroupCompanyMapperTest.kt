package com.clangengineer.exformmaker.service.mapper

import org.junit.jupiter.api.BeforeEach

class GroupCompanyMapperTest {

    private lateinit var groupCompanyMapper: GroupCompanyMapper

    @BeforeEach
    fun setUp() {
        groupCompanyMapper = GroupCompanyMapperImpl()
    }
}
