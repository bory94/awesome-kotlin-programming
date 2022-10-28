package com.bory.awesomekotlin.dsl.mvcservice

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

class FindAllServiceDSL<T : AbstractEntity>(
    var pageable: Pageable? = null,
    var findAllAction: ((Pageable) -> Page<T>)? = null,
) {
    companion object {
        fun <T : AbstractEntity> findAll(initialize: FindAllServiceDSL<T>.() -> Unit): Page<in T> =
            FindAllServiceDSL<T>().apply { initialize() }.execute()
    }

    fun execute(): Page<in T> = findAllAction!!.invoke(pageable!!).map(AbstractEntity::toDto)
}

class FindByIdServiceDSL<T : AbstractEntity>(
    var id: Long? = null,
    var findByIdAction: ((Long) -> Optional<T>)? = null
) {
    companion object {
        fun <T : AbstractEntity> findById(initialize: FindByIdServiceDSL<T>.() -> Unit): AbstractDto =
            FindByIdServiceDSL<T>().apply(initialize).execute()
    }

    fun execute(): AbstractDto =
        findByIdAction!!.invoke(id!!)
            .orElseThrow { IllegalArgumentException("Data ID[$id] Not Found") }
            .toDto()
}

class InsertServiceDSL<T : AbstractEntity>(
    var dto: AbstractDto? = null,
    var saveAction: ((T) -> T)? = null
) {
    companion object {
        fun <T : AbstractEntity> insert(initialize: InsertServiceDSL<T>.() -> Unit): AbstractDto =
            InsertServiceDSL<T>().apply(initialize).execute()
    }

    @Suppress("UNCHECKED_CAST")
    fun execute(): AbstractDto =
        saveAction!!.invoke(dto!!.toEntity() as T).toDto()
}

class UpdateServiceDSL<T : AbstractEntity>(
    var dto: AbstractDto? = null,
    var findAction: ((Long) -> Optional<T>)? = null,
    var saveAction: ((T) -> T)? = null
) {
    companion object {
        fun <T : AbstractEntity> update(initialize: UpdateServiceDSL<T>.() -> Unit): AbstractDto =
            UpdateServiceDSL<T>().apply(initialize).execute()
    }

    fun execute(): AbstractDto {
        val entity = findAction!!.invoke(dto!!.id!!)
            .orElseThrow { IllegalArgumentException("Data ID[${dto!!.id}] Not Found") }

        dto!!.copyTo(entity)

        return saveAction!!.invoke(entity).toDto()
    }
}
