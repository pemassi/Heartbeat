package io.pemassi.heartbeat.repository

import io.pemassi.heartbeat.entity.AlertEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AlertRepository: JpaRepository<AlertEntity, Int> {
}