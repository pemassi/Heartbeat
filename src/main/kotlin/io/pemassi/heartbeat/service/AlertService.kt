package io.pemassi.heartbeat.service

import io.pemassi.heartbeat.entity.AlertEntity
import io.pemassi.heartbeat.models.jpa.BasicCrud
import io.pemassi.heartbeat.repository.AlertRepository
import org.springframework.stereotype.Service

@Service
class AlertService(
    repository: AlertRepository
): BasicCrud<AlertEntity, Int>(repository)
{

}