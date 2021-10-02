package io.pemassi.heartbeat.models.rules.condition.details

import io.pemassi.heartbeat.global.SpringContext
import io.pemassi.heartbeat.models.rules.HeartBeatRule
import io.pemassi.heartbeat.models.rules.condition.ConditionMethod
import io.pemassi.heartbeat.models.rules.condition.common.TimeDuration
import io.pemassi.heartbeat.models.rules.test.TestResult
import io.pemassi.heartbeat.service.ConditionService
import io.pemassi.heartbeat.service.TestLogService
import io.pemassi.kotlin.extensions.slf4j.getLogger
import kotlinx.serialization.Serializable
import javax.validation.Valid
import javax.validation.constraints.Min

@Serializable
data class ConditionFailMoreThan(
    @Min(1)
    val times: Int,
    @Valid
    val duration: TimeDuration,
): ConditionDetail()
{
    override val method: ConditionMethod
        get() = ConditionMethod.FailMoreThan

    override fun validation() {
        duration.validation()
    }

    override fun isMeetCondition(rule: HeartBeatRule, conditionService: ConditionService): Boolean {
        val testLogService = SpringContext.getBean(TestLogService::class)
        val testLogList = testLogService.getAllWithTestHashAndDuration(rule.test.hashCode(), duration.toDuration())
        val countFailed = testLogList.count { it.result == TestResult.FAIL }

        logger.debug("ConditionFailMoreThan - condition: $times / failed: $countFailed")

        return if(countFailed >= times)
        {
            logger.info("Meet condition (condition: $times / failed: $countFailed)")
            true
        }
        else
        {
            false
        }
    }

    companion object
    {
        private val logger by getLogger()
    }
}