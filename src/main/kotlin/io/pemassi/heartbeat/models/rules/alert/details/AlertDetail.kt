package io.pemassi.heartbeat.models.rules.alert.details

import io.pemassi.heartbeat.models.rules.alert.AlertMethod
import io.pemassi.heartbeat.models.rules.test.TestLog
import kotlinx.serialization.Serializable
import org.springframework.context.ApplicationContext

@Serializable
sealed class AlertDetail {
    abstract val method: AlertMethod

    abstract fun reportConditionMet(testLog: TestLog, context: ApplicationContext)

    abstract fun reportRecovered(testLog: TestLog, context: ApplicationContext)

    abstract fun validation()
}