package io.pemassi.heartbeat.models.rules.alert

import io.pemassi.heartbeat.models.rules.alert.details.AlertConsole
import io.pemassi.heartbeat.models.rules.alert.details.AlertDetail
import io.pemassi.heartbeat.models.rules.alert.details.AlertEmail
import io.pemassi.heartbeat.models.rules.alert.details.AlertTelegram
import io.pemassi.heartbeat.models.rules.test.TestLog
import kotlinx.serialization.Serializable

@Serializable
data class AlertRule(
    //Contents
    val title: String,
    val body: String,

    //Rules
    val console: AlertConsole? = null,
    val telegram: AlertTelegram? = null,
    val email: AlertEmail? = null,
)
{
    val rules: List<AlertDetail>
        get() = listOfNotNull(console, telegram, email)

    fun validation()
    {
        require(rules.isNotEmpty()) { "At least one alert rule needs."}

        rules.forEach {
            it.validation()
        }
    }

    fun reportConditionMet(testLog: TestLog)
    {
        rules.map { it.reportConditionMet(testLog) }
    }

    fun reportRecovered(testLog: TestLog)
    {
        rules.map { it.reportRecovered(testLog) }
    }
}