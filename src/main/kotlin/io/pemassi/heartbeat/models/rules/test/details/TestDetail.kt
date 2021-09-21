package io.pemassi.heartbeat.models.rules.test.details

import io.pemassi.heartbeat.models.rules.HeartBeatRule
import io.pemassi.heartbeat.models.rules.test.TestMethod
import io.pemassi.heartbeat.models.rules.test.TestResult

interface TestDetail {
    val method: TestMethod

    fun validation()

    fun doTest(rule: HeartBeatRule): TestResult

}