package com.clangengineer.exformmaker.service.mapper

import org.junit.jupiter.api.BeforeEach

class UserGroupMapperTest {

    private lateinit var userGroupMapper: UserGroupMapper

    @BeforeEach
    fun setUp() {
        userGroupMapper = UserGroupMapperImpl()
    }
}
