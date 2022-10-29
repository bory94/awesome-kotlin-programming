package com.bory.awesomekotlin.dsl.mvcservice

import com.bory.awesomekotlin.dsl.mvcservice.FindAllServiceDSL.Companion.findAll
import com.bory.awesomekotlin.dsl.mvcservice.IdBasedService.Companion.delete
import com.bory.awesomekotlin.dsl.mvcservice.IdBasedService.Companion.findById
import com.bory.awesomekotlin.dsl.mvcservice.SaveServiceDSL.Companion.insert
import com.bory.awesomekotlin.dsl.mvcservice.SaveServiceDSL.Companion.update
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CustomerService(
    private val customerRepository: CustomerRepository
) {
    fun findAll(page: Pageable): Page<CustomerDto> = findAll {
        pageable = page
        findAllAction = customerRepository::findAll
    }

    fun findById(findingId: Long): CustomerDto = findById<CustomerEntity, CustomerDto> {
        id = findingId
        repository = customerRepository
    }

    fun insert(customer: CustomerDto): CustomerDto = insert {
        dto = customer
        saveAction = customerRepository::save
    }

    fun update(customer: CustomerDto): CustomerDto = update {
        dto = customer
        findAction = customerRepository::findById
        saveAction = customerRepository::save
    }

    fun delete(deletingId: Long): CustomerDto = delete<CustomerEntity, CustomerDto> {
        id = deletingId
        repository = customerRepository
    }
}