package io.pemassi.heartbeat.models.rules

import com.fasterxml.jackson.annotation.JsonIgnore
import io.pemassi.heartbeat.models.rules.alert.AlertRule
import io.pemassi.heartbeat.models.rules.condition.ConditionRule
import io.pemassi.heartbeat.models.rules.test.TestResult
import io.pemassi.heartbeat.models.rules.test.TestRule
import kotlinx.serialization.Serializable
import org.quartz.CronExpression
import org.quartz.CronScheduleBuilder

@Serializable
data class HeartBeatRule
(
    val name: String,
    val description: String = "",
    val cron: String = "0/10 * * ? * *", //Every min
    val test: TestRule,
    val condition: ConditionRule,
    val alert: AlertRule,
)
{
    @get:JsonIgnore
    val cronSchedule: CronScheduleBuilder by lazy {
        CronScheduleBuilder.cronSchedule(cron)
    }

    val jobName: String
        get() = "${name}_Job"

    val triggerName: String
        get() = "${name}_Trigger"

    fun validation()
    {
        require(CronExpression.isValidExpression(cron)) { "Cron is not valid expression." }

        test.validation()
        condition.validation()
        alert.validation()
    }

    fun performTest(): List<TestResult>
    {
        return test.performTest(this)
    }

    fun isMeetCondition(): List<Boolean>
    {
        return condition.isMeetCondition(this)
    }

    fun reportConditionMet(testResult: TestResult)
    {
        alert.reportConditionMet(testResult)
    }

    fun reportRecovered(testResult: TestResult)
    {
        alert.reportRecovered(testResult)
    }
}