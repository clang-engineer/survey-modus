package com.clangengineer.exformmaker.service.mapper

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat

class PointMapperTest {

    private lateinit var pointMapper: PointMapper

    @BeforeEach
    fun setUp() {
        pointMapper = PointMapperImpl()
    }
}
