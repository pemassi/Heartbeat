package io.pemassi.heartbeat.configurations

import org.quartz.CronScheduleBuilder
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "heartbeat")
data class HeartbeatConfiguration
(
    var rule: Rule = Rule()
)
{
    data class Rule
    (
            var update: Update = Update()
    )
    {
        data class Update
        (
            var enabled: Boolean = true,
            var cron: String = "0/10 * * ? * * *"
        )
        {
            val cronSchedule: CronScheduleBuilder by lazy {
                CronScheduleBuilder.cronSchedule(cron)
            }
        }
    }
}