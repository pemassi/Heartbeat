package io.pemassi.heartbeat.extensions

import org.quartz.JobExecutionContext

val JobExecutionContext.jobName: String
    get() = this.jobDetail.key.name

val JobExecutionContext.triggerName: String
    get() = this.trigger.key.name