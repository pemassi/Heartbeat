package io.pemassi.heartbeat.quartz.model

data class QuartzJobStatusResponse(
        val numOfAllJobs: Int,
        val numOfGroups: Int,
        val jobs: List<QuartzJobStatus>
)

