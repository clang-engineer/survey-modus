package com.clangengineer.surveymodus.config

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.*
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory
import org.springframework.http.HttpHeaders
import org.springframework.mock.env.MockEnvironment
import org.springframework.mock.web.MockServletContext
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder
import tech.jhipster.config.JHipsterConstants
import tech.jhipster.config.JHipsterProperties
import java.io.File
import javax.servlet.Filter
import javax.servlet.FilterRegistration
import javax.servlet.Servlet
import javax.servlet.ServletRegistration

/**
 * Unit tests for the [WebConfigurer] class.
 */
class WebConfigurerTest {

    private lateinit var webConfigurer: WebConfigurer

    private lateinit var servletContext: MockServletContext

    private lateinit var env: MockEnvironment

    private lateinit var props: JHipsterProperties

    @BeforeEach
    fun setup() {
        servletContext = spy(MockServletContext())
        doReturn(mock(FilterRegistration.Dynamic::class.java))
            .`when`(servletContext).addFilter(anyString(), any(Filter::class.java))
        doReturn(mock(ServletRegistration.Dynamic::class.java))
            .`when`(servletContext).addServlet(anyString(), any(Servlet::class.java))

        env = MockEnvironment()
        props = JHipsterProperties()

        webConfigurer = WebConfigurer(env, props)
    }

    @Test
    fun shouldCustomizeServletContainer() {
        env.setActiveProfiles(JHipsterConstants.SPRING_PROFILE_PRODUCTION)
        val container = UndertowServletWebServerFactory()
        webConfigurer.customize(container)
        assertThat(container.mimeMappings.get("abs")).isEqualTo("audio/x-mpeg")
        assertThat(container.mimeMappings.get("html")).isEqualTo("text/html")
        assertThat(container.mimeMappings.get("json")).isEqualTo("application/json")
        if (container.documentRoot != null) {
            assertThat(container.documentRoot).isEqualTo(File("build/resources/main/static/"))
        }
    }

    @Test
    @Throws(Exception::class)
    fun shouldCorsFilterOnApiPath() {
        props.cors.allowedOrigins = listOf("other.domain.com")
        props.cors.allowedMethods = listOf("GET", "POST", "PUT", "DELETE")
        props.cors.allowedHeaders = listOf("*")
        props.cors.maxAge = 1800L
        props.cors.allowCredentials = true

        val mockMvc = MockMvcBuilders.standaloneSetup(WebConfigurerTestController())
            .addFilters<StandaloneMockMvcBuilder>(webConfigurer.corsFilter())
            .build()

        mockMvc.perform(
            options("/api/test-cors")
                .header(HttpHeaders.ORIGIN, "other.domain.com")
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST")
        )
            .andExpect(status().isOk)
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "other.domain.com"))
            .andExpect(header().string(HttpHeaders.VARY, "Origin"))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET,POST,PUT,DELETE"))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true"))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "1800"))

        mockMvc.perform(
            get("/api/test-cors")
                .header(HttpHeaders.ORIGIN, "other.domain.com")
        )
            .andExpect(status().isOk)
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "other.domain.com"))
    }

    @Test
    @Throws(Exception::class)
    fun shouldCorsFilterOnOtherPath() {
        props.cors.allowedOrigins = listOf("*")
        props.cors.allowedMethods = listOf("GET", "POST", "PUT", "DELETE")
        props.cors.allowedHeaders = listOf("*")
        props.cors.maxAge = 1800L
        props.cors.allowCredentials = true

        val mockMvc = MockMvcBuilders.standaloneSetup(WebConfigurerTestController())
            .addFilters<StandaloneMockMvcBuilder>(webConfigurer.corsFilter())
            .build()

        mockMvc.perform(
            get("/test/test-cors")
                .header(HttpHeaders.ORIGIN, "other.domain.com")
        )
            .andExpect(status().isOk)
            .andExpect(header().doesNotExist(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN))
    }

    @Test
    @Throws(Exception::class)
    fun shouldCorsFilterDeactivatedForNullAllowedOrigins() {
        props.cors.allowedOrigins = null

        val mockMvc = MockMvcBuilders.standaloneSetup(WebConfigurerTestController())
            .addFilters<StandaloneMockMvcBuilder>(webConfigurer.corsFilter())
            .build()

        mockMvc.perform(
            get("/api/test-cors")
                .header(HttpHeaders.ORIGIN, "other.domain.com")
        )
            .andExpect(status().isOk)
            .andExpect(header().doesNotExist(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN))
    }

    @Test
    @Throws(Exception::class)
    fun shouldCorsFilterDeactivatedForEmptyAllowedOrigins() {
        props.cors.allowedOrigins = mutableListOf()

        val mockMvc = MockMvcBuilders.standaloneSetup(WebConfigurerTestController())
            .addFilters<StandaloneMockMvcBuilder>(webConfigurer.corsFilter())
            .build()

        mockMvc.perform(
            get("/api/test-cors")
                .header(HttpHeaders.ORIGIN, "other.domain.com")
        )
            .andExpect(status().isOk)
            .andExpect(header().doesNotExist(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN))
    }
}
