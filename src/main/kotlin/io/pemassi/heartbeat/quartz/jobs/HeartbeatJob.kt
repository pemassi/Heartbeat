package io.pemassi.heartbeat.quartz.jobs

import io.pemassi.heartbeat.entity.TestLogEntity
import io.pemassi.heartbeat.extensions.jobName
import io.pemassi.heartbeat.extensions.triggerName
import io.pemassi.heartbeat.models.rules.HeartBeatRule
import io.pemassi.heartbeat.models.rules.test.TestResult
import io.pemassi.heartbeat.quartz.const.QuartzConst
import io.pemassi.heartbeat.service.AlertService
import io.pemassi.heartbeat.service.ConditionService
import io.pemassi.heartbeat.service.TestLogService
import io.pemassi.heartbeat.service.TestService
import io.pemassi.kotlin.extensions.google.globalGson
import io.pemassi.kotlin.extensions.slf4j.getLogger
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.quartz.*
import org.springframework.context.ApplicationContext
import org.springframework.scheduling.quartz.QuartzJobBean

class HeartbeatJob(
    private val testLogService: TestLogService,
    private val alertService: AlertService,
    private val conditionService: ConditionService,
    private val testService: TestService,
    private val applicationContext: ApplicationContext,
) : QuartzJobBean()
{

    override fun executeInternal(context: JobExecutionContext) {
        try
        {
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
            val testResultList = heartBeatRule.performTest(applicationContext)

            //Write log
            testLogService.insertAll(testResultList.map { TestLogEntity.of(it) })

            //Check Condition and Alert
            for (testResult in testResultList)
            {
                if(testResult.result == TestResult.SUCCESS)
                {
                    logger.debug("[$ruleName] Pass")

                    //Check this rule was reported, then it means recovered
                    testResult.reportRecovered(applicationContext)
                }
                else
                {
                    logger.error("[$ruleName] Fail, try to alert.")

                    //Condition Check
                    val isMeetCondition = testResult.rule.isMeetCondition(applicationContext)

                    if(isMeetCondition.any { it })
                    {
                        testResult.reportConditionMet(applicationContext)
                    }
                }
            }
        }
        catch(e: Exception)
        {
            logger.error("There was an exception during monitoring heartbeat.", e)
        }
    }

    companion object
    {
        private val logger by getLogger()
        private val json = Json { encodeDefaults = false }

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
                .usingJobData(QuartzConst.JobData.DATA_CLASS_JSON, json.encodeToString(heartBeatRule))
                .build()
        }
    }


}