package com.clangengineer.surveymodus.service

import com.clangengineer.surveymodus.security.STAFF
import com.clangengineer.surveymodus.security.getCurrentUserLogin
import com.clangengineer.surveymodus.service.dto.StaffDTO
import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

@Service
@Transactional
class StaffService(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun checkActivatedStaffExist(phone: String): Boolean {
        log.debug("Request to check Staff exist : $phone")
        val sql = "SELECT COUNT(*) FROM tbl_staff WHERE activated = true AND phone = :phone"

        val params = mapOf("phone" to phone)

        val count = jdbcTemplate.queryForObject(sql, params, Int::class.java)

        return count != null && count > 0
    }

    fun getStaffSession(): Optional<StaffDTO> {
        log.debug("REST request to get staff info")

        val phone = getCurrentUserLogin().orElseThrow { RuntimeException("User could not be found") }

        val sql = """
        SELECT first_name, last_name, email, activated, lang_key, phone
        FROM tbl_staff
        WHERE phone = :phone
        """.trimIndent()

        val params = mapOf("phone" to phone)

        val result = jdbcTemplate.query(
            sql, params
        ) { rs, _ ->
            StaffDTO(
                firstName = rs.getString("first_name"),
                lastName = rs.getString("last_name"),
                email = rs.getString("email"),
                activated = rs.getBoolean("activated"),
                langKey = rs.getString("lang_key"),
                phone = rs.getString("phone"),
                authorities = mutableSetOf(STAFF)
            )
        }.firstOrNull()

        return Optional.ofNullable(result)
    }
}
