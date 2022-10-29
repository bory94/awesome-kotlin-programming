package com.bory.awesomekotlin.dsl.mvcservice

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

@Suppress("UNCHECKED_CAST")
class FindAllServiceDSL<E : AbstractEntity, D : AbstractDto>(
    var pageable: Pageable? = null,
    var findAllAction: ((Pageable) -> Page<E>)? = null,
) {
    companion object {
        fun <E : AbstractEntity, D : AbstractDto> findAll(initialize: FindAllServiceDSL<E, D>.() -> Unit): Page<D> =
            FindAllServiceDSL<E, D>().apply { initialize() }.execute()
    }

    fun execute(): Page<D> = findAllAction!!.invoke(pageable!!).map { it.toDto() as D }
}

@Suppress("UNCHECKED_CAST")
class IdBasedService<E : AbstractEntity, D : AbstractDto>(
    var id: Long? = null,
    var repository: JpaRepository<E, Long>? = null
) {
    companion object {
        fun <E : AbstractEntity, D : AbstractDto> findById(initialize: IdBasedService<E, D>.() -> Unit): D =
            IdBasedService<E, D>().apply(initialize).findById()

        fun <E : AbstractEntity, D : AbstractDto> delete(initialize: IdBasedService<E, D>.() -> Unit) =
            IdBasedService<E, D>().apply(initialize).delete()
    }

    fun findById(): D {
        if (id == null || repository == null) throw IllegalArgumentException()

        return repository!!.findById(id!!)
            .orElseThrow { IllegalArgumentException("Data ID[$id] Not Found") }
            .toDto() as D
    }

    fun delete(): D {
        if (id == null || repository == null) throw IllegalArgumentException()

        val entity = repository!!.findById(id!!)
            .orElseThrow { IllegalArgumentException("Data ID[$id] Not Found") }

        repository!!.delete(entity)

        return (entity.toDto()) as D
    }

}

@Suppress("UNCHECKED_CAST")
class SaveServiceDSL<E : AbstractEntity, D : AbstractDto>(
    var dto: AbstractDto? = null,
    var findAction: ((Long) -> Optional<E>)? = null,
    var saveAction: ((E) -> E)? = null
) {
    companion object {
        fun <E : AbstractEntity, D : AbstractDto> insert(initialize: SaveServiceDSL<E, D>.() -> Unit): D =
            SaveServiceDSL<E, D>().apply(initialize).executeInsert()

        fun <E : AbstractEntity, D : AbstractDto> update(initialize: SaveServiceDSL<E, D>.() -> Unit): D =
            SaveServiceDSL<E, D>().apply(initialize).executeUpdate()
    }

    fun executeInsert(): D {
        if (dto == null || saveAction == null) throw IllegalArgumentException()

        return saveAction!!.invoke(dto!!.toEntity() as E).toDto() as D
    }

    fun executeUpdate(): D {
        if (dto == null || findAction == null || saveAction == null) throw IllegalArgumentException()

        val entity = findAction!!.invoke(dto!!.id!!)
            .orElseThrow { IllegalArgumentException("Data ID[${dto!!.id}] Not Found") }

        dto!!.copyTo(entity)

        return saveAction!!.invoke(entity).toDto() as D
    }
}
