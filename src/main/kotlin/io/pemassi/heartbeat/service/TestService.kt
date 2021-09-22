package io.pemassi.heartbeat.service

import io.pemassi.heartbeat.entity.TestEntity
import io.pemassi.heartbeat.models.jpa.BasicCrud
import io.pemassi.heartbeat.repository.TestRepository
import org.springframework.stereotype.Service

@Service
class TestService(
    repository: TestRepository
): BasicCrud<TestEntity, Int>(repository)
{

}