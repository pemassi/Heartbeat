package io.pemassi.heartbeat.entity

import io.pemassi.heartbeat.models.rules.test.TestLog
import io.pemassi.heartbeat.models.rules.test.TestResult
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hibernate.Hibernate
import javax.persistence.*

@Entity
data class TestLogEntity(
    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @ManyToOne
    val test: TestEntity,

    @Enumerated(EnumType.STRING)
    val result: TestResult,

    val departureHostAddress: String,

    val departureHostName: String,

    val departurePort: Int?,

    val destinationHost: String?,

    val destinationPort: Int?,

    val errorMessage: String?,

    @Lob
    val additionalParamMap: String,
): BaseTimeEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(
                other
            )
        ) return false
        other as TestLogEntity

        return id == other.id
    }

    override fun hashCode(): Int = 0

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , createdDate = $createdDate , modifiedDate = $modifiedDate , test = $test , result = $result , departureHostAddress = $departureHostAddress , departureHostName = $departureHostName , departurePort = $departurePort , destinationHost = $destinationHost , destinationPort = $destinationPort , errorMessage = $errorMessage , additionalParamMap = $additionalParamMap )"
    }

    companion object
    {
        fun of(testLog: TestLog): TestLogEntity
        {
            return TestLogEntity(
                test = TestEntity.of(testLog.testDetail),
                result = testLog.result,
                departureHostAddress = testLog.departureHostAddress,
                departureHostName = testLog.departureHostName,
                departurePort = testLog.departurePort,
                destinationHost = testLog.destinationHost,
                destinationPort = testLog.destinationPort,
                errorMessage = testLog.errorMessage,
                additionalParamMap = Json.encodeToString(testLog.additionalParamMap)
            )
        }
    }
}