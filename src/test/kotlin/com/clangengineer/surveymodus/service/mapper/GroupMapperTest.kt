package com.clangengineer.surveymodus.service.mapper

import org.junit.jupiter.api.BeforeEach

class GroupMapperTest {
    private lateinit var groupMapper: GroupMapper

    @BeforeEach
    fun setUp() {
        groupMapper = GroupMapperImpl()
    }
}
