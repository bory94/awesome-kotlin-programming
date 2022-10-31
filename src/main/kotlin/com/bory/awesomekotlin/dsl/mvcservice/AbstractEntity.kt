package com.bory.awesomekotlin.dsl.mvcservice

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.data.util.ProxyUtils
import java.time.Instant
import java.util.*
import javax.persistence.*

@MappedSuperclass
@EntityListeners(value = [AuditingEntityListener::class])
abstract class AbstractEntity<E : AbstractEntity<E, D>, D : AbstractDto<D, E>>(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "created_at", updatable = false)
    @CreatedDate
    var createdAt: Instant? = null,

    @LastModifiedDate
    @Column(name = "modified_at")
    var modifiedAt: Instant? = null
) {
    abstract fun newDto(): D

    override fun equals(other: Any?): Boolean {
        other ?: return false
        if (this === other) return true
        if (javaClass != ProxyUtils.getUserClass(other)) return false
        other as AbstractEntity<*, *>

        return if (null == this.id) false else this.id == other.id
    }

    override fun hashCode(): Int {
        return Objects.hash(this.javaClass.name + "$id")
    }

    override fun toString() = "Entity of type ${this.javaClass.name} with id: $id"
}