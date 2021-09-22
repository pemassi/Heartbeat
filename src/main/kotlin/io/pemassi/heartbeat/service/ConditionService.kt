package io.pemassi.heartbeat.service

import io.pemassi.heartbeat.entity.ConditionEntity
import io.pemassi.heartbeat.models.jpa.BasicCrud
import io.pemassi.heartbeat.repository.ConditionRepository
import org.springframework.stereotype.Service

@Service
class ConditionService(
    repository: ConditionRepository
): BasicCrud<ConditionEntity, Int>(repository)
{

}