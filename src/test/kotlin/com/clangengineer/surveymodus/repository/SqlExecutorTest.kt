package com.clangengineer.surveymodus.repository

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.jdbc.core.JdbcTemplate
import java.sql.ResultSet

class SqlExecutorTest {

    private val jdbcTemplate: JdbcTemplate = mock(JdbcTemplate::class.java)
    private val sqlExecutor = SqlExecutor(jdbcTemplate)

    @Test
    fun `executeSelectAll should return list of maps with lowercase keys`() {
        val mockResult = listOf(
            mapOf("KEY1" to "value1", "KEY2" to "value2"),
            mapOf("KEY3" to "value3", "KEY4" to "value4")
        )

        `when`(jdbcTemplate.queryForList(anyString())).thenReturn(mockResult)

        val result = sqlExecutor.executeSelectAll("SELECT * FROM test_table")

        assertEquals(2, result.size)
        assertEquals("value1", result[0]["key1"])
        assertEquals("value3", result[1]["key3"])
        verify(jdbcTemplate, times(1)).queryForList(anyString())
    }

    @Test
    fun `executeSelectOne should return map with lowercase keys`() {
        val mockResult = mapOf("KEY1" to "value1", "KEY2" to "value2")

        `when`(jdbcTemplate.queryForMap(anyString())).thenReturn(mockResult)

        val result = sqlExecutor.executeSelectOne("SELECT * FROM test_table WHERE id = 1")

        assertEquals("value1", result["key1"])
        assertEquals("value2", result["key2"])
        verify(jdbcTemplate, times(1)).queryForMap(anyString())
    }

    @Test
    fun `executeSelectOne should return empty map if result is empty`() {
        `when`(jdbcTemplate.queryForMap(anyString())).thenReturn(emptyMap())

        val result = sqlExecutor.executeSelectOne("SELECT * FROM test_table WHERE id = 1")

        assertTrue(result.isEmpty())
        verify(jdbcTemplate, times(1)).queryForMap(anyString())
    }

    @Test
    fun `executeDML should return true for successful update`() {
        `when`(jdbcTemplate.update(anyString())).thenReturn(1)

        val result = sqlExecutor.executeDML("UPDATE test_table SET column = 'value' WHERE id = 1")

        assertTrue(result)
        verify(jdbcTemplate, times(1)).update(anyString())
    }

    @Test
    fun `executeDML should return false for no rows updated`() {
        `when`(jdbcTemplate.update(anyString())).thenReturn(0)

        val result = sqlExecutor.executeDML("UPDATE test_table SET column = 'value' WHERE id = 1")

        assertFalse(result)
        verify(jdbcTemplate, times(1)).update(anyString())
    }

    @Test
    fun `keyChangeLowerMap should convert all keys to lowercase`() {
        val inputMap = mapOf("KEY1" to "value1", "KEY2" to "value2")
        val expectedMap = mapOf("key1" to "value1", "key2" to "value2")

        val result = SqlExecutor.keyChangeLowerMap(inputMap)

        assertEquals(expectedMap, result)
    }
}
