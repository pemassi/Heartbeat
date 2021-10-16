package io.pemassi.heartbeat.repository

import io.pemassi.heartbeat.entity.TestEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TestRepository: JpaRepository<TestEntity, Int> {
}