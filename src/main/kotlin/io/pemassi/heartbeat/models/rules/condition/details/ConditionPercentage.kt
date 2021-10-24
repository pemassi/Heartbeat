package io.pemassi.heartbeat.models.rules.condition.details

import io.pemassi.heartbeat.models.rules.HeartBeatRule
import io.pemassi.heartbeat.models.rules.condition.ConditionMethod
import io.pemassi.heartbeat.models.rules.condition.common.TimeDuration
import io.pemassi.heartbeat.models.rules.test.TestResult
import io.pemassi.heartbeat.service.TestLogService
import io.pemassi.kotlin.extensions.slf4j.getLogger
import io.pemassi.kotlin.extensions.spring.getBean
import kotlinx.serialization.Serializable
import org.hibernate.validator.constraints.Range
import org.springframework.context.ApplicationContext
import java.security.InvalidParameterException
import javax.validation.Valid

@Serializable
data class ConditionPercentage(
    @Range(min = 0, max = 1)
    val lower: Float = 1.0f,
    @Range(min = 0, max = 1)
    val bigger: Float = 0.0f,
    @Valid
    val duration: TimeDuration,
): ConditionDetail()
{
    override val method: ConditionMethod
        get() = ConditionMethod.Percentage

    override fun validation() {
        duration.validation()

        if(lower < bigger)
            throw InvalidParameterException("'bigger' cannot be bigger than 'lower'.")
    }

    override fun isMeetCondition(rule: HeartBeatRule, context: ApplicationContext): Boolean {
        val testLogService = context.getBean(TestLogService::class)
        val testLogList = testLogService.getAllWithTestHashAndDuration(rule.test, duration.toDuration())

        //Calculation
        //TODO - Should count 'ERROR' also?
        val countSuccess = testLogList.count { it.result == TestResult.SUCCESS }
        val countFailed = testLogList.count { it.result == TestResult.FAIL }
        val percentage = countSuccess / (countFailed + countSuccess)

        logger.debug("ConditionPercentage - $bigger < $percentage < $lower")

        return if(bigger < percentage && percentage < lower)
        {
            logger.info("Meet condition!")
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