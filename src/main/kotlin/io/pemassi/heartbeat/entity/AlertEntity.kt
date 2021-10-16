package io.pemassi.heartbeat.entity

import io.pemassi.heartbeat.models.rules.alert.AlertMethod
import io.pemassi.heartbeat.models.rules.alert.details.AlertDetail
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hibernate.Hibernate
import javax.persistence.*

@Entity
data class AlertEntity(
    @Id
    val hash: Int,

    @Enumerated(EnumType.STRING)
    val method: AlertMethod,

    @Lob
    val detail: String,
): BaseTimeEntity() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(
                other
            )
        ) return false
        other as AlertEntity

        return hash == other.hash
    }

    override fun hashCode(): Int = 0

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(hash = $hash , method = $method , detail = $detail , createdDate = $createdDate , modifiedDate = $modifiedDate )"
    }

    companion object
    {
        fun of(detail: AlertDetail): AlertEntity
        {
            return AlertEntity(
                hash = detail.hashCode(),
                method = detail.method,
                detail = Json.encodeToString(detail)
            )
        }
    }
}
