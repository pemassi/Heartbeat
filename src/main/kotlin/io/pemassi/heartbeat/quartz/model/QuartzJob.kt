package io.pemassi.heartbeat.quartz.model

import org.quartz.JobDetail
import org.quartz.Trigger

interface QuartzJob
{
    fun getTrigger(): Trigger
    fun getJobDetail(): JobDetail
}