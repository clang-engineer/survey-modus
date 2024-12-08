package com.clangengineer.exformmaker.service.mapper

import org.junit.jupiter.api.BeforeEach

class GroupUserMapperTest {

    private lateinit var groupUserMapper: GroupUserMapper

    @BeforeEach
    fun setUp() {
        groupUserMapper = GroupUserMapperImpl()
    }
}
