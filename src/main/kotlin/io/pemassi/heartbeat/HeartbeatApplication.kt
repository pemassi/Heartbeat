package io.pemassi.heartbeat

import io.pemassi.heartbeat.configurations.HeartbeatConfiguration
import io.pemassi.heartbeat.quartz.jobs.RuleSyncJob
import io.pemassi.heartbeat.service.QuartzService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@EnableFeignClients
@SpringBootApplication
class HeartbeatApplication

fun main(args: Array<String>) {
	val context = runApplication<HeartbeatApplication>(*args)

	//Create RuleSyncJob
	val heartbeatConfiguration = context.getBean(HeartbeatConfiguration::class.java)
	val quartzService = context.getBean(QuartzService::class.java)

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
