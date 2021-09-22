package io.pemassi.heartbeat.quartz.jobs

import io.pemassi.heartbeat.entity.TestLogEntity
import io.pemassi.heartbeat.extensions.jobName
import io.pemassi.heartbeat.extensions.triggerName
import io.pemassi.heartbeat.global.TestTracking
import io.pemassi.heartbeat.models.rules.HeartBeatRule
import io.pemassi.heartbeat.models.rules.test.TestResult
import io.pemassi.heartbeat.quartz.const.QuartzConst
import io.pemassi.heartbeat.service.TestLogService
import io.pemassi.kotlin.extensions.google.globalGson
import io.pemassi.kotlin.extensions.slf4j.getLogger
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.quartz.*
import org.springframework.scheduling.quartz.QuartzJobBean

class HeartbeatJob(
    private val testLogService: TestLogService,
) : QuartzJobBean()
{

    override fun executeInternal(context: JobExecutionContext) {
        //Get job information
        val jobKey = context.jobName
        val trigger = context.triggerName

        logger.debug("[$jobKey/$trigger] Start to process")

        //Get Job Data
        val dataJson = context.jobDetail.jobDataMap[QuartzConst.JobData.DATA_CLASS_JSON] as String
        val heartBeatRule = globalGson.fromJson(dataJson, HeartBeatRule::class.java)

        //Start
        val ruleName = heartBeatRule.name
        logger.debug("[$ruleName] Start to test")

        //Test
        val testResultList = heartBeatRule.performTest()

        //Write log
        testLogService.insertAll(testResultList.map { TestLogEntity.of(it) })

        //Check Condition and Alert
        for (testResult in testResultList)
        {
            if(testResult.result == TestResult.SUCCESS)
            {
                logger.debug("[$ruleName] Pass")

                //Check this rule was reported, then it means recovered
                if(TestTracking.isAlerted(ruleName))
                {
                    TestTracking.recovered(ruleName)
                    testResult.reportRecovered()
                }
            }
            else
            {
                logger.error("[$ruleName] Fail, try to alert.")

                //Update test tracking.
                TestTracking.upFailCount(ruleName)

                //Condition Check
                val isMeetCondition = testResult.rule.isMeetCondition()

                if(isMeetCondition.any { !it })
                {
                    testResult.reportConditionMet()
                }
            }
        }
    }

    companion object
    {
        private val logger by getLogger()

        fun createTrigger(heartBeatRule: HeartBeatRule): Trigger {
            return TriggerBuilder.newTrigger()
                .withIdentity(heartBeatRule.triggerName, HeartbeatJob::class.simpleName)
                .withDescription(heartBeatRule.description)
                .withSchedule(heartBeatRule.cronSchedule)
                .build()
        }

        fun createJob(heartBeatRule: HeartBeatRule): JobDetail {
            return JobBuilder.newJob()
                .ofType(HeartbeatJob::class.java)
                .storeDurably()
                .withIdentity(heartBeatRule.jobName,  HeartbeatJob::class.simpleName)
                .withDescription(heartBeatRule.description)
                .usingJobData(QuartzConst.JobData.DATA_CLASS_JSON, Json { encodeDefaults = false }.encodeToString(heartBeatRule))
                .build()
        }
    }


}