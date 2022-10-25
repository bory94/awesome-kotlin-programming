package com.bory.awesomekotlin.dsl.basic

import java.time.LocalDateTime
import java.util.*

data class Reservation(
    val id: UUID = UUID.randomUUID(),
    var reservationTime: LocalDateTime = LocalDateTime.now(),
    var customer: Customer = Customer()
) {
    fun customer(initialize: Customer.() -> Unit) {
        this.customer = Customer().apply {
            initialize()
        }
    }

    fun validateSelf() {
        if (reservationTime < LocalDateTime.now()) throw IllegalArgumentException("Reservation Time should be after now")
        this.customer.validateSelf()
    }
}

data class Customer(
    var name: String = "",
    var numberOfAccompanies: Int = 0
) {
    fun validateSelf() {
        if (name.isEmpty()) throw IllegalArgumentException("Name Cannot be Empty")
        if (numberOfAccompanies < 0) throw IllegalArgumentException("Number of Accompanies should be positive")
    }
}