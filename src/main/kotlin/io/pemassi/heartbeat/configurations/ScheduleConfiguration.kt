package io.pemassi.heartbeat.configurations

import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled

@Configuration
@EnableScheduling
class ScheduleConfiguration(
        val jobLauncher: JobLauncher,
        val batchConfiguration: BatchConfiguration,
        val heartbeatConfiguration: HeartbeatConfiguration)
{

    @Scheduled(fixedRateString = "#{@heartbeatConfiguration.getDuration()}")
    fun perform()
    {
        val params = JobParametersBuilder()
                .addString("JobID", System.currentTimeMillis().toString())
                .toJobParameters()

        jobLauncher.run(batchConfiguration.makeJob(), params)
    }

}