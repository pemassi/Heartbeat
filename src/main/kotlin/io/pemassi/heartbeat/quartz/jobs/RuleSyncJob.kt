package io.pemassi.heartbeat.quartz.jobs

import io.pemassi.heartbeat.configurations.HeartbeatConfiguration
import io.pemassi.heartbeat.configurations.RuleConfiguration
import io.pemassi.heartbeat.entity.AlertEntity
import io.pemassi.heartbeat.entity.ConditionEntity
import io.pemassi.heartbeat.entity.TestEntity
import io.pemassi.heartbeat.global.SpringContext
import io.pemassi.heartbeat.models.rules.HeartBeatRule
import io.pemassi.heartbeat.service.AlertService
import io.pemassi.heartbeat.service.ConditionService
import io.pemassi.heartbeat.service.QuartzService
import io.pemassi.heartbeat.service.TestService
import io.pemassi.heartbeat.util.YamlParser
import io.pemassi.kotlin.extensions.slf4j.getLogger
import org.quartz.*
import org.springframework.boot.SpringApplication
import org.springframework.scheduling.quartz.QuartzJobBean
import java.io.File
import kotlin.system.exitProcess

class RuleSyncJob(
    private val ruleConfiguration: RuleConfiguration,
    private val quartzService: QuartzService,
    private val testService: TestService,
    private val conditionService: ConditionService,
    private val alertService: AlertService,
) : QuartzJobBean()
{
    override fun executeInternal(context: JobExecutionContext)
    {
        //Read Rules
        val rules = ArrayList<HeartBeatRule>()
        val folder = File(ruleConfiguration.location)

        if(!folder.exists())
        {
            exitApplication("Rule location(${folder.absolutePath}) is not exist!!")
        }

        if(!folder.isDirectory)
        {
            exitApplication("Rule location(${folder.absolutePath}) is not folder!!")
        }

        folder.listFiles()?.let {
            for(file in it)
            {
                if(file.isFile && file.extension == "yaml")
                {
                    rules.add(YamlParser.parse(file, HeartBeatRule.serializer()))
                }
            }
        }

        //Check duplicate name
        val duplicatedList = rules.groupingBy { it }.eachCount().filter { it.value > 1 }
        if(duplicatedList.isNotEmpty())
        {
            logger.error("There is/are duplicated name(s). Stop Sync. \n\r $duplicatedList")
            return
        }

        //Update database
        for(rule in rules)
        {
            for(test in rule.test.rules)
                testService.insert(TestEntity.of(test))

            for(condition in rule.condition.rules)
                conditionService.insert(ConditionEntity.of(condition))

            for(alert in rule.alert.rules)
                alertService.insert(AlertEntity.of(alert))
        }

        //Delete Old Jobs
        val jobList = quartzService.getAllJobs().jobs.filter { it.groupName == HeartbeatJob::class.simpleName }

        for(job in jobList)
        {
            val rule = rules.find { job.jobName == it.jobName }

            if(rule == null)
            {
                logger.debug("Unschedule deleted job [${job.jobName}].")
                quartzService.unscheduleJob(JobKey.jobKey(job.jobName, job.groupName))
            }
        }

        //Schedule New Jobs
        for(rule in rules)
        {
            val job = HeartbeatJob.createJob(rule)
            val trigger = HeartbeatJob.createTrigger(rule)

            //Override Job (since some data could be updated)
            quartzService.scheduleJob(job, trigger)
        }
    }

    private fun exitApplication(message: String)
    {
        logger.error(message)
        exitProcess(SpringApplication.exit(SpringContext.context))
    }

    companion object
    {
        private val logger by getLogger()

        fun createTrigger(heartbeatConfiguration: HeartbeatConfiguration): Trigger {
            return TriggerBuilder.newTrigger()
                .withIdentity("RuleSyncJob_Trigger", RuleSyncJob::class.simpleName)
                .withDescription("Sync HeartBeat Rules")
                .withSchedule(heartbeatConfiguration.rule.update.cronSchedule)
                .build()
        }

        fun createOnceTrigger(heartbeatConfiguration: HeartbeatConfiguration): Trigger {
            return TriggerBuilder.newTrigger()
                .withIdentity("RuleSyncJob_Trigger", RuleSyncJob::class.simpleName)
                .withDescription("Sync HeartBeat Rules")
                .build()
        }

        fun createJob(heartbeatConfiguration: HeartbeatConfiguration): JobDetail {
            return JobBuilder.newJob()
                .ofType(RuleSyncJob::class.java)
                .storeDurably()
                .withIdentity("RuleSyncJob_Job",  RuleSyncJob::class.simpleName)
                .withDescription("Sync HeartBeat Rules")
                .build()
        }
    }
}