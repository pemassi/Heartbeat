package io.pemassi.heartbeat.entity

import io.pemassi.heartbeat.models.rules.condition.ConditionMethod
import io.pemassi.heartbeat.models.rules.condition.details.ConditionDetail
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hibernate.Hibernate
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id

@Entity
data class ConditionEntity(
    @Id
    val hash: Int,

    @Enumerated(EnumType.STRING)
    val method: ConditionMethod,

    val detail: String,
): BaseTimeEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(
                other
            )
        ) return false
        other as ConditionEntity

        return hash == other.hash
    }

    override fun hashCode(): Int = 0

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(hash = $hash , createdDate = $createdDate , modifiedDate = $modifiedDate , method = $method , json = $detail )"
    }

    companion object
    {
        fun of(detail: ConditionDetail): ConditionEntity
        {
            return ConditionEntity(
                hash = detail.hashCode(),
                method = detail.method,
                detail = Json.encodeToString(detail)
            )
        }
    }
}
