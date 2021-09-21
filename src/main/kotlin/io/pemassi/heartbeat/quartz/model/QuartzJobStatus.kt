package io.pemassi.heartbeat.quartz.model

import org.quartz.Trigger

data class QuartzJobStatus(
        val jobName: String,
        val groupName: String,
        val triggers: List<Trigger>
)