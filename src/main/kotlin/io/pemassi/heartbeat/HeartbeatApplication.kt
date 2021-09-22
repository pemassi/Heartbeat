package io.pemassi.heartbeat

import io.pemassi.heartbeat.configurations.HeartbeatConfiguration
import io.pemassi.heartbeat.global.SpringContext
import io.pemassi.heartbeat.quartz.jobs.RuleSyncJob
import io.pemassi.heartbeat.service.QuartzService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class HeartbeatApplication

fun main(args: Array<String>) {
	val context = runApplication<HeartbeatApplication>(*args)

	//Create RuleSyncJob
	val heartbeatConfiguration = SpringContext.getBean(HeartbeatConfiguration::class)
	val quartzService = SpringContext.getBean(QuartzService::class)

	val job = RuleSyncJob.createJob(heartbeatConfiguration)
	val trigger = if(heartbeatConfiguration.rule.update.enabled)
	{
		RuleSyncJob.createTrigger(heartbeatConfiguration)
	}
	else
	{
		RuleSyncJob.createOnceTrigger(heartbeatConfiguration)
	}

	quartzService.scheduleJob(job, trigger)
}
