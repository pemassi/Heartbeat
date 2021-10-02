package io.pemassi.heartbeat.repository

import io.pemassi.heartbeat.entity.TestLogEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface TestLogRepository: JpaRepository<TestLogEntity, Long> {
    fun findAllByTest_HashEqualsAndCreatedDateBetween(testHash: Int, start: LocalDateTime, end: LocalDateTime): List<TestLogEntity>
}