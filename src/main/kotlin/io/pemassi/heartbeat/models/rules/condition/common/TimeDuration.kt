package io.pemassi.heartbeat.models.rules.condition.common

import kotlinx.serialization.Serializable
import org.hibernate.validator.constraints.Range
import java.time.Duration

@Serializable
data class TimeDuration(
    @Range(min = 0, max = 23)
    val hours: Int = 0,
    @Range(min = 0, max = 59)
    val minutes: Int = 0,
    @Range(min = 0, max = 59)
    val seconds: Int = 0,
)
{
    fun validation()
    {
        require(!(hours == 0 && minutes == 0 && seconds == 0)) { "Cannot be zero second." }
    }

    fun toDuration(): Duration
    {
        return Duration.ofSeconds(seconds.toLong() + (minutes.toLong() * 60L) + (hours.toLong() * 3600L))
    }
}