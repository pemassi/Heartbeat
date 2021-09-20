package io.pemassi.heartbeat.batch.writer

import io.pemassi.kotlin.extensions.slf4j.getLogger
import io.pemassi.heartbeat.global.TestTracking
import io.pemassi.heartbeat.models.TestResult
import io.pemassi.heartbeat.models.rules.alert.AlertConditionMethod
import io.pemassi.heartbeat.models.rules.alert.AlertMethod
import org.springframework.batch.item.ItemWriter

class AlertRuleWriter: ItemWriter<TestResult>
{
    private val logger by getLogger()

    override fun write(items: MutableList<out TestResult>)
    {
        for (item in items)
        {
            val ruleName = item.rule.name
            val conditonMethod = item.rule.alert.condition.method

            if(item.result)
            {
                logger.debug("[$ruleName] Pass")

                //Check this rule was reported, then it means recovered
                if(TestTracking.isAlerted(ruleName))
                {
                    TestTracking.recovered(ruleName)
                    report(item)
                }
            }
            else
            {
                logger.error("[$ruleName] Fail, try to alert.")

                //Update test tracking.
                TestTracking.upFailCount(ruleName)

                when(conditonMethod)
                {
                    AlertConditionMethod.FailMoreThan ->
                    {
                        val conditionTimes = item.rule.alert.condition.times ?: throw Exception("There is no condition times.")
                        val failedTimes = TestTracking.getFailCount(ruleName)

                        logger.debug("[$ruleName] Alert Condition Fail [$failedTimes/$conditionTimes]")
                        if(failedTimes > conditionTimes)
                        {
                            report(item)
                        }
                    }
                    else -> throw Exception("Unsupported condition method.")
                }
            }
        }
    }

    fun report(item: TestResult)
    {
        val ruleName = item.rule.name

        if(!item.result)
        {
            if(TestTracking.isAlerted(ruleName))
            {
                logger.debug("[$ruleName] It's already alerted, skip.")
                return
            }

            TestTracking.alerted(item.rule.name)
        }

        when(item.rule.alert.method)
        {
            AlertMethod.Telegram -> TelegramAlertWriter().write(item)
            AlertMethod.Console -> ConsoleAlertWriter().write(item)

            else -> throw Exception("Unsupported alert method.")
        }
    }
}