package com.clangengineer.surveymodus.service

import com.clangengineer.surveymodus.repository.CompanyRepository
import com.clangengineer.surveymodus.service.dto.CompanyDTO
import com.clangengineer.surveymodus.service.mapper.CompanyMapper
import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AuthorizedCompanyService(
    private val jdbcTemplate: JdbcTemplate,
    private val companyRepository: CompanyRepository,
    private val companyMapper: CompanyMapper
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun fetchCompaniesByStaffPhone(phone: String): List<CompanyDTO> {
        log.debug("Request to fetch companies by staff phone : $phone")

        val sql = """
            SELECT id FROM tbl_company c
            JOIN tbl_staff s ON c.id = s.company_id
            WHERE s.phone = ?
        """.trimIndent()

        val companyIds = jdbcTemplate.query(
            sql,
            arrayOf(phone)
        ) { rs, _ -> rs.getLong("id") }

        return companyIds.mapNotNull { id ->
            companyRepository.findById(id)
                .map { companyMapper.toDto(it) }
                .orElse(null)
        }
    }
}
