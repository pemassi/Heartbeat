package io.pemassi.heartbeat.models.jpa

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional

abstract class BasicCrud<T: Any, ID: Any>(
    protected open val repository: JpaRepository<T, ID>
) {
    @Transactional
    open fun getAll(pageable: Pageable): Page<T> = repository.findAll(pageable)

    @Transactional
    open fun getById(id: ID): T? = repository.findByIdOrNull(id)

    @Transactional
    open fun insert(obj: T): T = repository.save(obj)

    @Transactional
    open fun insertAll(obj: List<T>): List<T> = obj.map { insert(it) }

    @Transactional
    open fun update(obj: T): T = repository.save(obj)

    @Transactional
    open fun updateAll(obj: List<T>): List<T> = obj.map { update(it) }

    @Transactional
    open fun deleteById(id: ID): T? = getById(id)?.also {
        repository.delete(it)
    }
}