package com.bory.awesomekotlin.dsl.mvcservice

import java.time.Instant

abstract class AbstractDto(
    val id: Long?,
    val createdAt: Instant = Instant.now(),
    val modifiedAt: Instant = Instant.now()
) {
    abstract fun toEntity(): AbstractEntity
    abstract fun copyTo(entity: AbstractEntity)
}