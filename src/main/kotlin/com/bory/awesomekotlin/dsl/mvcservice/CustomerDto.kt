package com.bory.awesomekotlin.dsl.mvcservice

import java.time.Instant

class CustomerDto(
    id: Long? = null,
    val name: String = "Anonymous",
    val age: Int = 0,
    createdAt: Instant = Instant.now(),
    modifiedAt: Instant = Instant.now()
) : AbstractDto<CustomerDto, CustomerEntity>(id, createdAt, modifiedAt) {
    override fun newEntity() = CustomerEntity(id, name, age, createdAt, modifiedAt)
    override fun copyTo(entity: CustomerEntity) {
        entity.age = age
        entity.name = name
    }
}
