package com.bory.awesomekotlin.dsl.mvcservice

import org.springframework.data.jpa.repository.JpaRepository

interface CustomerRepository : JpaRepository<CustomerEntity, Long>