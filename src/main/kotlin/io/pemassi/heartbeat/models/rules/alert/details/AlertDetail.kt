package io.pemassi.heartbeat.models.rules.alert.details

import io.pemassi.heartbeat.models.rules.alert.AlertMethod
import io.pemassi.heartbeat.models.rules.test.TestLog
import kotlinx.serialization.Serializable

@Serializable
sealed class AlertDetail {
    abstract val method: AlertMethod

    abstract fun reportConditionMet(testLog: TestLog)

    abstract fun reportRecovered(testLog: TestLog)

    abstract fun validation()
}