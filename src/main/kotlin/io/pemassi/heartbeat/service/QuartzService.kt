package io.pemassi.heartbeat.service

import io.pemassi.heartbeat.quartz.model.QuartzJobStatus
import io.pemassi.heartbeat.quartz.model.QuartzJobStatusResponse
import io.pemassi.kotlin.extensions.slf4j.getLogger
import org.quartz.JobDetail
import org.quartz.JobKey
import org.quartz.Trigger
import org.quartz.impl.matchers.GroupMatcher
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import org.springframework.stereotype.Service

@Service
class QuartzService(
        private val schedulerFactoryBean: SchedulerFactoryBean
)
{
    private val logger by getLogger()

    fun scheduleJob(jobDetail: JobDetail, trigger: Trigger): JobDetail
    {
        if(isJobExists(jobDetail.key))
            unscheduleJob(jobDetail.key)

        val dt = schedulerFactoryBean.scheduler.scheduleJob(jobDetail, trigger)

        logger.debug("Job with jobKey : {} scheduled successfully at date : {}", jobDetail.key, dt)

        return jobDetail
    }

    fun unscheduleJob(jobKey: JobKey)
    {
        schedulerFactoryBean.scheduler.deleteJob(jobKey)
    }

    fun isJobExists(jobKey: JobKey): Boolean
    {
        return schedulerFactoryBean.scheduler.checkExists(jobKey)
    }

    fun getAllJobs(): QuartzJobStatusResponse
    {
        val jobs = ArrayList<QuartzJobStatus>()

        val scheduler = schedulerFactoryBean.scheduler
        for (groupName in scheduler.jobGroupNames)
        {
            for (jobKey in scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName)))
            {
                jobs.add(
                    QuartzJobStatus(
                        jobName = jobKey.name,
                        groupName = jobKey.group,
                        triggers = scheduler.getTriggersOfJob(jobKey)
                    )
                )
            }
        }

        return QuartzJobStatusResponse(
                numOfAllJobs = jobs.size,
                numOfGroups = scheduler.jobGroupNames.size,
                jobs = jobs
        )
    }
}