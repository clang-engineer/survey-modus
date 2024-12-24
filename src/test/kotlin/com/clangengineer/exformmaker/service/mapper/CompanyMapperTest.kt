package com.clangengineer.exformmaker.service.mapper

import org.junit.jupiter.api.BeforeEach

class CompanyMapperTest {
  private lateinit var companyMapper: CompanyMapper

  @BeforeEach
  fun setUp() {
    companyMapper = CompanyMapperImpl()
  }
}
