package io.pemassi.heartbeat.models.rules.test.details

import io.pemassi.heartbeat.models.rules.HeartBeatRule
import io.pemassi.heartbeat.models.rules.test.TestLog
import io.pemassi.heartbeat.models.rules.test.TestMethod
import io.pemassi.heartbeat.service.TestService
import kotlinx.serialization.Serializable

@Serializable
sealed class TestDetail {
    abstract val method: TestMethod

    abstract fun validation()

    abstract fun performTest(rule: HeartBeatRule, testService: TestService): TestLog
}