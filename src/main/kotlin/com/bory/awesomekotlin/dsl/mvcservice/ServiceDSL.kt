package com.bory.awesomekotlin.dsl.mvcservice

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

class FindAllServiceDSL<E : AbstractEntity<E, D>, D : AbstractDto<D, E>>(
    var pageable: Pageable? = null,
    var findAllAction: ((Pageable) -> Page<E>)? = null,
) {
    companion object {
        fun <E : AbstractEntity<E, D>, D : AbstractDto<D, E>> findAll(initialize: FindAllServiceDSL<E, D>.() -> Unit): Page<D> =
            FindAllServiceDSL<E, D>().apply { initialize() }.execute()
    }

    fun execute(): Page<D> = findAllAction!!.invoke(pageable!!).map { it.newDto() }
}

class IdBasedService<E : AbstractEntity<E, D>, D : AbstractDto<D, E>>(
    var id: Long? = null,
    var repository: JpaRepository<E, Long>? = null
) {
    companion object {
        fun <E : AbstractEntity<E, D>, D : AbstractDto<D, E>> findById(initialize: IdBasedService<E, D>.() -> Unit): D =
            IdBasedService<E, D>().apply(initialize).findById()

        fun <E : AbstractEntity<E, D>, D : AbstractDto<D, E>> delete(initialize: IdBasedService<E, D>.() -> Unit) =
            IdBasedService<E, D>().apply(initialize).delete()
    }

    fun findById(): D {
        if (id == null || repository == null) throw IllegalArgumentException()

        return repository!!.findById(id!!)
            .orElseThrow { IllegalArgumentException("Data ID[$id] Not Found") }
            .newDto()
    }

    fun delete(): D {
        if (id == null || repository == null) throw IllegalArgumentException()

        val entity = repository!!.findById(id!!)
            .orElseThrow { IllegalArgumentException("Data ID[$id] Not Found") }

        repository!!.delete(entity)

        return entity.newDto()
    }

}

class SaveServiceDSL<E : AbstractEntity<E, D>, D : AbstractDto<D, E>>(
    var dto: AbstractDto<D, E>? = null,
    var findAction: ((Long) -> Optional<E>)? = null,
    var saveAction: ((E) -> E)? = null
) {
    companion object {
        fun <E : AbstractEntity<E, D>, D : AbstractDto<D, E>> insert(initialize: SaveServiceDSL<E, D>.() -> Unit): D =
            SaveServiceDSL<E, D>().apply(initialize).executeInsert()

        fun <E : AbstractEntity<E, D>, D : AbstractDto<D, E>> update(initialize: SaveServiceDSL<E, D>.() -> Unit): D =
            SaveServiceDSL<E, D>().apply(initialize).executeUpdate()
    }

    fun executeInsert(): D {
        if (dto == null || saveAction == null) throw IllegalArgumentException()

        return saveAction!!.invoke(dto!!.newEntity()).newDto()
    }

    fun executeUpdate(): D {
        if (dto == null || findAction == null || saveAction == null) throw IllegalArgumentException()

        val entity = findAction!!.invoke(dto!!.id!!)
            .orElseThrow { IllegalArgumentException("Data ID[${dto!!.id}] Not Found") }

        dto!!.copyTo(entity)

        return saveAction!!.invoke(entity).newDto()
    }
}
