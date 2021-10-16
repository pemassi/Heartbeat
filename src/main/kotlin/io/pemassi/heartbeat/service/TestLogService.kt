package io.pemassi.heartbeat.service

import io.pemassi.heartbeat.entity.TestLogEntity
import io.pemassi.heartbeat.models.jpa.BasicCrud
import io.pemassi.heartbeat.models.rules.test.TestRule
import io.pemassi.heartbeat.repository.TestLogRepository
import io.pemassi.kotlin.extensions.slf4j.getLogger
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.LocalDateTime

@Service
class TestLogService(
    override val repository: TestLogRepository
): BasicCrud<TestLogEntity, Long>(repository)
{
    fun getAllWithTestHashAndDuration(testRule: TestRule, duration: Duration): List<TestLogEntity>
    {
        val start = LocalDateTime.now().minus(duration)
        val end = LocalDateTime.now()

        logger.debug("getBeforeDuration - start: $start / end: $end / duration: $duration")

        return repository.findAllByTest_HashEqualsAndCreatedDateBetween(
            testHash = testRule.hashCode(),
            start = start,
            end = end,
        )
    }

    companion object
    {
        private val logger by getLogger()
    }
}