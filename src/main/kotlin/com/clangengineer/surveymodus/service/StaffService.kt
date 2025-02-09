package com.clangengineer.surveymodus.service

import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class StaffService(
    private val jdbcTemplate: JdbcTemplate
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun checkActivatedStaffExist(phone: String): Boolean {
        log.debug("Request to check Staff exist : $phone")
        val sql = "SELECT COUNT(*) FROM tbl_staff WHERE activated = true AND phone = ?"

        val count = jdbcTemplate.queryForObject(sql, arrayOf(phone), RowMapper { rs, _ -> rs.getInt(1) })

        return count != null && count > 0
    }
}
