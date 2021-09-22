package io.pemassi.heartbeat.repository

import io.pemassi.heartbeat.entity.ConditionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ConditionRepository: JpaRepository<ConditionEntity, Int> {
}