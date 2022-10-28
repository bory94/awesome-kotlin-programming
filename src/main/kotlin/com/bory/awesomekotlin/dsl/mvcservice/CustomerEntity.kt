package com.bory.awesomekotlin.dsl.mvcservice

import java.time.Instant
import javax.persistence.Entity

@Entity
class CustomerEntity(
    id: Long? = null,
    var name: String,
    var age: Int = 0,
    createdAt: Instant? = Instant.now(),
    modifiedAt: Instant? = Instant.now()
) : AbstractEntity(id, createdAt, modifiedAt) {
    override fun toDto() =
        CustomerDto(
            id, name, age, createdAt!!, modifiedAt!!
        )
}