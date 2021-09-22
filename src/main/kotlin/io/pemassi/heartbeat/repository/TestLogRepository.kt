package io.pemassi.heartbeat.repository

import io.pemassi.heartbeat.entity.TestLogEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TestLogRepository: JpaRepository<TestLogEntity, Long> {
}