package com.clangengineer.surveymodus.service

import com.clangengineer.surveymodus.IntegrationTest
import com.clangengineer.surveymodus.domain.embeddable.Staff
import com.clangengineer.surveymodus.web.rest.CompanyResourceIT
import com.clangengineer.surveymodus.web.rest.GroupResourceIT
import com.clangengineer.surveymodus.web.rest.UserResourceIT
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@IntegrationTest
@Transactional
@WithMockUser
class AuthorizedCompanyServiceIT {
    @Autowired
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var authorizedCompanyService: AuthorizedCompanyService

    @Test
    fun `test fetch companies by staff phone`() {
        val phone = "1234567890"
        val staff = Staff(
            firstName = "test_first_name",
            lastName = "test_last_name",
            email = "test_email@test.com",
            activated = true,
            langKey = "en",
            phone = phone
        )

        // Create 10 companies and add the staff to them
        for (i in 1..10) {
            val company = CompanyResourceIT.createEntity(em)
            val staffs = company.staffs
            staffs.add(staff)
            company.staffs = staffs
            em.persist(company)
            em.flush()
        }
        val companies = authorizedCompanyService.fetchCompaniesByStaffPhone(phone)

        assertThat(companies).hasSize(10)
    }

    @Test
    fun `fetch companies in activated group by user login`() {
        val group = GroupResourceIT.createEntity(em)
        val company = CompanyResourceIT.createEntity(em)
        val user = UserResourceIT.createEntity(em)

        em.persist(company)
        em.persist(user)
        em.flush()

        group.activated = true
        group.companies = mutableSetOf(company)
        group.users = mutableSetOf(user)

        em.persist(group)
        em.flush()

        val companies = authorizedCompanyService.fetchCompaniesInGroupByUserLogin(user.login!!)

        assertThat(companies).hasSize(1)
        assertThat(companies[0].id).isEqualTo(company.id)
    }

    @Test
    fun `fetch companies in deactived group by user login`() {
        val group = GroupResourceIT.createEntity(em)
        val company = CompanyResourceIT.createEntity(em)
        val user = UserResourceIT.createEntity(em)

        em.persist(company)
        em.persist(user)
        em.flush()

        group.activated = false
        group.companies = mutableSetOf(company)
        group.users = mutableSetOf(user)

        em.persist(group)
        em.flush()

        val companies = authorizedCompanyService.fetchCompaniesInGroupByUserLogin(user.login!!)

        assertThat(companies).isEmpty()
    }
}
