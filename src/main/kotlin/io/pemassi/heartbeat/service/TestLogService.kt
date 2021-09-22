package io.pemassi.heartbeat.service

import io.pemassi.heartbeat.entity.TestLogEntity
import io.pemassi.heartbeat.models.jpa.BasicCrud
import io.pemassi.heartbeat.repository.TestLogRepository
import org.springframework.stereotype.Service

@Service
class TestLogService(
    repository: TestLogRepository
): BasicCrud<TestLogEntity, Long>(repository)
{

}