package io.pemassi.heartbeat.models.rules.alert.details

import io.pemassi.heartbeat.models.rules.alert.AlertMethod
import io.pemassi.heartbeat.models.rules.test.TestResult
import io.pemassi.kotlin.extensions.slf4j.getLogger
import kotlinx.serialization.Serializable

@Serializable
data class AlertConsole(
    val debug: Boolean = false
): AlertDetail
{
    override val method: AlertMethod
        get() = AlertMethod.Console

    override fun validation() {

    }

    override fun reportConditionMet(testResult: TestResult) {
        report(testResult)
    }

    override fun reportRecovered(testResult: TestResult) {
        report(testResult)
    }

    private fun report(testResult: TestResult)
    {
        logger.info(testResult.buildAlertTitleAndBody())
    }

    companion object
    {
        private val logger by getLogger()
    }

}