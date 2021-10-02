package io.pemassi.heartbeat.models.rules.alert.details

import io.pemassi.heartbeat.models.rules.alert.AlertMethod
import io.pemassi.heartbeat.models.rules.test.TestLog
import io.pemassi.heartbeat.service.AlertService
import io.pemassi.kotlin.extensions.slf4j.getLogger
import kotlinx.serialization.Serializable

@Serializable
data class AlertConsole(
    val debug: Boolean = false
): AlertDetail()
{
    override val method: AlertMethod
        get() = AlertMethod.Console

    override fun validation() {

    }

    override fun reportConditionMet(testLog: TestLog, alertService: AlertService) {
        report(testLog)
    }

    override fun reportRecovered(testLog: TestLog, alertService: AlertService) {
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