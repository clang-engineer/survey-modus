package com.clangengineer.surveymodus.service.mapper

import org.junit.jupiter.api.BeforeEach

class UserPointMapperTest {

    private lateinit var userPointMapper: UserPointMapper

    @BeforeEach
    fun setUp() {
        userPointMapper = UserPointMapperImpl()
    }
}
