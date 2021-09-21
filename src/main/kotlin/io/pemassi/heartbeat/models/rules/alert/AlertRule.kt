package io.pemassi.heartbeat.models.rules.alert

import io.pemassi.heartbeat.models.rules.alert.details.AlertConsole
import io.pemassi.heartbeat.models.rules.alert.details.AlertDetail
import io.pemassi.heartbeat.models.rules.alert.details.AlertTelegram
import io.pemassi.heartbeat.models.rules.test.TestResult
import kotlinx.serialization.Serializable

@Serializable
data class AlertRule(
    //Contents
    val title: String,
    val body: String,

    //Rules
    val console: AlertConsole? = null,
    val telegram: AlertTelegram? = null,
)
{
    val rules: List<AlertDetail>
        get() = listOfNotNull(console, telegram)

    fun validation()
    {
        require(rules.isNotEmpty()) { "At least one alert rule needs."}

        rules.forEach {
            it.validation()
        }
    }

    fun reportConditionMet(testResult: TestResult)
    {
        rules.map { it.reportConditionMet(testResult) }
    }

    fun reportRecovered(testResult: TestResult)
    {
        rules.map { it.reportRecovered(testResult) }
    }
}