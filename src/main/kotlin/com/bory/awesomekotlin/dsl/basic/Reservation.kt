package com.bory.awesomekotlin.dsl.basic

import java.time.LocalDateTime
import java.util.*

data class Reservation(
    val id: UUID,
    val reservationTime: LocalDateTime,
    val customer: Customer
) {
    fun validateSelf() {
        if (reservationTime < LocalDateTime.now()) throw IllegalArgumentException("Reservation Time should be after now")
        this.customer.validateSelf()
    }
}

data class ReservationDSL(
    val id: UUID = UUID.randomUUID(),
    var reservationTime: LocalDateTime = LocalDateTime.now(),
    var customerDsl: CustomerDSL = CustomerDSL()
) {
    fun toReservation() =
        Reservation(id = id, reservationTime = reservationTime, customer = customerDsl.toCustomer())

    fun customer(initialize: CustomerDSL.() -> Unit) {
        this.customerDsl = CustomerDSL().apply(initialize)
    }
}

data class Customer(
    val name: String,
    val numberOfAccompanies: Int
) {
    fun validateSelf() {
        if (name.isEmpty()) throw IllegalArgumentException("Name Cannot be Empty")
        if (numberOfAccompanies < 0) throw IllegalArgumentException("Number of Accompanies should be positive")
    }
}

data class CustomerDSL(
    var name: String = "",
    var numberOfAccompanies: Int = 0
) {
    fun toCustomer() = Customer(name = name, numberOfAccompanies = numberOfAccompanies)
}
