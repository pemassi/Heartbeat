package io.pemassi.heartbeat.models.rules.alert.details

import io.pemassi.heartbeat.models.rules.alert.AlertMethod
import io.pemassi.heartbeat.models.rules.test.TestLog
import io.pemassi.kotlin.extensions.slf4j.getLogger
import kotlinx.serialization.Serializable
import org.springframework.context.ApplicationContext

@Serializable
data class AlertConsole(
    val debug: Boolean = false
): AlertDetail()
{
    override val method: AlertMethod
        get() = AlertMethod.Console

    override fun validation() {

    }

    override fun reportConditionMet(testLog: TestLog, context: ApplicationContext) {
        report(testLog)
    }

    override fun reportRecovered(testLog: TestLog, context: ApplicationContext) {
        report(testLog)
    }

    private fun report(testLog: TestLog)
    {
        logger.info(testLog.buildAlertTitleAndBody())
    }

    companion object
    {
        private val logger by getLogger()
    }

}