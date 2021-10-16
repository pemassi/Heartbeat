package io.pemassi.heartbeat.models.rules.test.details

import io.pemassi.heartbeat.models.rules.HeartBeatRule
import io.pemassi.heartbeat.models.rules.test.TestLog
import io.pemassi.heartbeat.models.rules.test.TestMethod
import kotlinx.serialization.Serializable
import org.springframework.context.ApplicationContext

@Serializable
sealed class TestDetail {
    abstract val method: TestMethod

    abstract fun validation()

    abstract fun performTest(rule: HeartBeatRule, context: ApplicationContext): TestLog
}