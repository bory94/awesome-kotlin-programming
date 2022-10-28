package com.bory.awesomekotlin.dsl.mvcservice

import java.time.Instant

class CustomerDto(
    id: Long? = null,
    val name: String = "Anonymous",
    val age: Int = 0,
    createdAt: Instant = Instant.now(),
    modifiedAt: Instant = Instant.now()
) : AbstractDto(id, createdAt, modifiedAt) {
    override fun toEntity() = CustomerEntity(id, name, age, createdAt, modifiedAt)
    override fun copyTo(entity: AbstractEntity) {
        if (entity !is CustomerEntity) {
            throw IllegalArgumentException("Entity is not a kind of Customer Entity")
        }

        entity.age = age
        entity.name = name
    }
}
