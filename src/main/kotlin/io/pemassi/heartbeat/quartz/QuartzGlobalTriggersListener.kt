package io.pemassi.heartbeat.quartz

import io.pemassi.kotlin.extensions.slf4j.getLogger
import org.quartz.JobExecutionContext
import org.quartz.Trigger
import org.quartz.TriggerListener
import org.springframework.stereotype.Component


@Component
class QuartzGlobalTriggersListener : TriggerListener
{
    private val logger by getLogger()

    override fun getName(): String
    {
        return "QuartzGlobalTriggersListener"
    }

    override fun triggerFired(trigger: Trigger, context: JobExecutionContext)
    {
        val jobKey = trigger.jobKey
        logger.info("triggerFired at {} :: jobKey : {}", trigger.startTime, jobKey)
    }

    override fun vetoJobExecution(trigger: Trigger, context: JobExecutionContext): Boolean
    {
        return false
    }

    override fun triggerMisfired(trigger: Trigger)
    {
        val jobKey = trigger.jobKey
        logger.info("triggerMisfired at {} :: jobKey : {}", trigger.startTime, jobKey)
    }

    override fun triggerComplete(trigger: Trigger, context: JobExecutionContext, triggerInstructionCode: Trigger.CompletedExecutionInstruction?)
    {
        val jobKey = trigger.jobKey
        logger.info("triggerComplete at {} :: jobKey : {}", trigger.startTime, jobKey)
    }
}