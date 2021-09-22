package io.pemassi.heartbeat.entity

import io.pemassi.heartbeat.models.rules.test.TestMethod
import io.pemassi.heartbeat.models.rules.test.details.TestDetail
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hibernate.Hibernate
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id

@Entity
data class TestEntity(
    @Id
    val hash: Int,

    @Enumerated(EnumType.STRING)
    val method: TestMethod,

    val detail: String,
): BaseTimeEntity() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(
                other
            )
        ) return false
        other as TestEntity

        return hash == other.hash
    }

    override fun hashCode(): Int = 0

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(hash = $hash , createdDate = $createdDate , modifiedDate = $modifiedDate , method = $method , detail = $detail )"
    }

    companion object
    {
        fun of(testDetail: TestDetail): TestEntity
        {
            return TestEntity(
                hash = testDetail.hashCode(),
                method = testDetail.method,
                detail = Json.encodeToString(testDetail),
            )
        }
    }
}
