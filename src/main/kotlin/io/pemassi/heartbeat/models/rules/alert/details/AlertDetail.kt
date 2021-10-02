package io.pemassi.heartbeat.models.rules.alert.details

import io.pemassi.heartbeat.models.rules.alert.AlertMethod
import io.pemassi.heartbeat.models.rules.test.TestLog
import io.pemassi.heartbeat.service.AlertService
import kotlinx.serialization.Serializable

@Serializable
sealed class AlertDetail {
    abstract val method: AlertMethod

    abstract fun reportConditionMet(testLog: TestLog, alertService: AlertService)

    abstract fun reportRecovered(testLog: TestLog, alertService: AlertService)

    abstract fun validation()
}