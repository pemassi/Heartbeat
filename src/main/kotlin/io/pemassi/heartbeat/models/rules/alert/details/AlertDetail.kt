package io.pemassi.heartbeat.models.rules.alert.details

import io.pemassi.heartbeat.models.rules.alert.AlertMethod
import io.pemassi.heartbeat.models.rules.test.TestResult

interface AlertDetail {
    val method: AlertMethod

    fun reportConditionMet(testResult: TestResult)

    fun reportRecovered(testResult: TestResult)

    fun validation()
}