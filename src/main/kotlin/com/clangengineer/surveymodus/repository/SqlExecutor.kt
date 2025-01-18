package com.clangengineer.surveymodus.repository

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.util.*

@Repository
class SqlExecutor(jdbcTemplate: JdbcTemplate) {
    val jdbcTemplate: JdbcTemplate

    init {
        this.jdbcTemplate = jdbcTemplate
    }

    val sequenceNextValue
        get() = jdbcTemplate.query<Long>("SELECT NEXTVAL('sequence_generator')") { rs: ResultSet ->
            if (rs.next()) {
                return@query rs.getLong(1)
            } else {
                throw RuntimeException("Unable to retrieve value from sequence sequence_generator.")
            }
        }

    fun executeSelectAll(sql: String): List<Map<String, Any>> {
        val result: List<Map<String, Any>> = jdbcTemplate.queryForList(sql)

        return result.map { keyChangeLowerMap(it) }
    }

    fun executeSelectOne(sql: String): Map<String, Any> {
        val result: Map<String, Any> = jdbcTemplate.queryForMap(sql)

        return result.size.let {
            if (it == 0) {
                emptyMap<String, Any>()
            } else {
                keyChangeLowerMap(result)
            }
        }
    }

    fun executeDML(sql: String?): Boolean {
        return jdbcTemplate.update(sql) > 0
    }

    companion object {
        fun keyChangeLowerMap(param: Map<String, Any>): Map<String, Any> {
            val iteratorKey = param.keys.iterator()
            val newMap: MutableMap<String, Any> = HashMap()
            while (iteratorKey.hasNext()) {
                val key = iteratorKey.next()
                newMap[key.lowercase(Locale.getDefault())] = param[key]!!
            }
            return newMap
        }
    }
}
