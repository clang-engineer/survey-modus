package com.clangengineer.surveymodus

import com.clangengineer.surveymodus.config.AsyncSyncConfiguration
import com.clangengineer.surveymodus.config.EmbeddedSQL
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.TestPropertySource

/**
 * Base composite annotation for integration tests.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@SpringBootTest(classes = [SurveyModusApp::class, AsyncSyncConfiguration::class])
@EmbeddedSQL
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestPropertySource(properties = ["spring.mongodb.embedded.version=4.0.3"])
annotation class IntegrationTest
