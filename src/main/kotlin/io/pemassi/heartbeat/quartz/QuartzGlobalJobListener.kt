package io.pemassi.heartbeat.quartz

import io.pemassi.kotlin.extensions.slf4j.getLogger
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException
import org.quartz.JobListener
import org.springframework.stereotype.Component


@Component
class QuartzGlobalJobListener: JobListener
{
    private val logger by getLogger()

    override fun getName(): String
    {
        return "QuartzGlobalJobListener"
    }

    override fun jobToBeExecuted(context: JobExecutionContext)
    {
        val jobKey = context.jobDetail.key
        logger.info("jobToBeExecuted :: jobKey : {}", jobKey)
    }

    override fun jobExecutionVetoed(context: JobExecutionContext)
    {
        val jobKey = context.jobDetail.key
        logger.info("jobExecutionVetoed :: jobKey : {}", jobKey)
    }

    override fun jobWasExecuted(context: JobExecutionContext, jobException: JobExecutionException?)
    {
        val jobKey = context.jobDetail.key
        logger.info("jobWasExecuted :: jobKey : {}", jobKey)
    }

}