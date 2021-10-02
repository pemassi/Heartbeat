package io.pemassi.heartbeat.models.rules

import com.fasterxml.jackson.annotation.JsonIgnore
import io.pemassi.heartbeat.models.rules.alert.AlertRule
import io.pemassi.heartbeat.models.rules.condition.ConditionRule
import io.pemassi.heartbeat.models.rules.test.TestLog
import io.pemassi.heartbeat.models.rules.test.TestRule
import io.pemassi.heartbeat.service.AlertService
import io.pemassi.heartbeat.service.ConditionService
import io.pemassi.heartbeat.service.TestService
import kotlinx.serialization.Serializable
import org.quartz.CronExpression
import org.quartz.CronScheduleBuilder
import javax.validation.constraints.NotBlank

@Serializable
data class HeartBeatRule
(
    @NotBlank
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

    fun performTest(testService: TestService): List<TestLog>
    {
        return test.performTest(this, testService)
    }

    fun isMeetCondition(conditionService: ConditionService): List<Boolean>
    {
        return condition.isMeetCondition(this, conditionService)
    }

    fun reportConditionMet(testLog: TestLog, alertService: AlertService)
    {
        alert.reportConditionMet(testLog, alertService)
    }

    fun reportRecovered(testLog: TestLog, alertService: AlertService)
    {
        alert.reportRecovered(testLog, alertService)
    }
}