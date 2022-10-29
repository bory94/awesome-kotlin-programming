package com.bory.awesomekotlin.dsl.mvcservice

import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/customer")
class CustomerController(
    private val customerService: CustomerService
) {
    @GetMapping
    fun findAll(pageable: Pageable) = customerService.findAll(pageable)

    @GetMapping("/{id}")
    fun findById(@PathVariable("id") id: Long): CustomerDto = customerService.findById(id)

    @PostMapping
    fun insert(@Valid @RequestBody customerDto: CustomerDto) = customerService.insert(customerDto)

    @PutMapping
    fun update(@Valid @RequestBody customerDto: CustomerDto) = customerService.update(customerDto)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: Long): CustomerDto = customerService.delete(id)
}