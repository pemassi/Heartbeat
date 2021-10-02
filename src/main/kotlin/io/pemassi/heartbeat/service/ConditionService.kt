package io.pemassi.heartbeat.service

import io.pemassi.heartbeat.entity.ConditionEntity
import io.pemassi.heartbeat.models.jpa.BasicCrud
import io.pemassi.heartbeat.repository.ConditionRepository
import io.pemassi.kotlin.extensions.slf4j.getLogger
import org.springframework.stereotype.Service

@Service
class ConditionService(
    override val repository: ConditionRepository
): BasicCrud<ConditionEntity, Int>(repository)
{
    companion object
    {
        private val logger by getLogger()
    }
}