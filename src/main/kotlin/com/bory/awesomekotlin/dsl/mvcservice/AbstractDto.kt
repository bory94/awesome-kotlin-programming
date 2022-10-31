package com.bory.awesomekotlin.dsl.mvcservice

import java.time.Instant

abstract class AbstractDto<D : AbstractDto<D, E>, E : AbstractEntity<E, D>>(
    val id: Long?,
    val createdAt: Instant = Instant.now(),
    val modifiedAt: Instant = Instant.now()
) {
    abstract fun newEntity(): E
    abstract fun copyTo(entity: E)
}