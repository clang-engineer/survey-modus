package com.clangengineer.surveymodus.service

import com.clangengineer.surveymodus.IntegrationTest
import com.clangengineer.surveymodus.domain.embeddable.Staff
import com.clangengineer.surveymodus.web.rest.CompanyResourceIT
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@IntegrationTest
@Transactional
@WithMockUser
class StaffServiceIT {
    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Autowired
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var staffService: StaffService

    @Test
    fun `test fetch companies by staff phone`() {
        val company = CompanyResourceIT.createEntity(em)

        val staffs = company.staffs

        val staff = Staff(
            firstName = "test_first_name",
            lastName = "test_last_name",
            email = "test_email@test.com",
            activated = true,
            langKey = "en",
            phone = "1234567890"
        )

        staffs.add(staff)
        company.staffs = staffs

        em.persist(company)
        em.flush()

        val companies = staffService.fetchCompaniesByStaffPhone("1234567890")

        assertEquals(1, companies.size)
        assertEquals(company.id, companies[0].id)
    }
}
