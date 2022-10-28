package com.bory.awesomekotlin.dsl.mvcservice

import com.bory.awesomekotlin.dsl.mvcservice.FindAllServiceDSL.Companion.findAll
import com.bory.awesomekotlin.dsl.mvcservice.FindByIdServiceDSL.Companion.findById
import com.bory.awesomekotlin.dsl.mvcservice.InsertServiceDSL.Companion.insert
import com.bory.awesomekotlin.dsl.mvcservice.UpdateServiceDSL.Companion.update
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CustomerService(
    private val customerRepository: CustomerRepository
) {
    fun findAll(page: Pageable) = findAll<CustomerEntity> {
        pageable = page
        findAllAction = customerRepository::findAll
    }

    fun findById(findingId: Long) = findById<CustomerEntity> {
        id = findingId
        findByIdAction = customerRepository::findById

    } as CustomerDto

    fun insert(customer: CustomerDto): CustomerDto = insert {
        dto = customer
        saveAction = customerRepository::save
    } as CustomerDto

    fun update(customer: CustomerDto) = update<CustomerEntity> {
        dto = customer
        findAction = customerRepository::findById
        saveAction = customerRepository::save
    }
}