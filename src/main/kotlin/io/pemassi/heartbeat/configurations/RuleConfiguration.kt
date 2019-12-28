package io.pemassi.heartbeat.configurations

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "heartbeat.rule")
data class RuleConfiguration
(
    var location: String = "rules"
)