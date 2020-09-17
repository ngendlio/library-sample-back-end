package com.example.library.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.hibernate.annotations.Type
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.util.*
import java.util.UUID
import javax.persistence.*

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
@JsonIgnoreProperties(
        value = ["createdAt", "updatedAt"],
        allowGetters = true
)
/**
 * This class contains the DB model fields that keep coming in all
 * the tables. example: Id, creationDates, updateDates.
 */
open class BaseModel : Serializable {
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    var createdAt: Date = Date()

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    var updatedAt: Date = Date()

    @PreUpdate
    fun onUpdate() {
        this.updatedAt = Date()
    }

    @Id
    val id: UUID = UUID.randomUUID()

    override fun hashCode(): Int = id.hashCode()
}



