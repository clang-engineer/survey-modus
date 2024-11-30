package com.clangengineer.exformmaker

import com.clangengineer.exformmaker.config.AsyncSyncConfiguration
import com.clangengineer.exformmaker.config.EmbeddedSQL
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext

/**
 * Base composite annotation for integration tests.
 */
@kotlin.annotation.Target(AnnotationTarget.CLASS)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@SpringBootTest(classes = [ExformmakerApp::class, AsyncSyncConfiguration::class])
@EmbeddedSQL
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
annotation class IntegrationTest
