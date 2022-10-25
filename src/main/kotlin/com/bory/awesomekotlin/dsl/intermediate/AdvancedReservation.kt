package com.bory.awesomekotlin.dsl.intermediate

import java.time.LocalDateTime
import java.util.*

data class AdvancedReservation(
    val id: UUID,
    val reservationTime: LocalDateTime,
    val advancedCustomer: AdvancedCustomer
) {
    fun validateSelf() {
        if (reservationTime < LocalDateTime.now()) throw IllegalArgumentException("Reservation Time should be after now")
        this.advancedCustomer.validateSelf()
    }
}

data class AdvancedReservationDSL(
    val id: UUID = UUID.randomUUID(),
    var reservationTime: LocalDateTime = LocalDateTime.now(),
    var advancedCustomerDsl: AdvancedCustomerDSL = AdvancedCustomerDSL()
) {
    fun toReservation() =
        AdvancedReservation(
            id = id,
            reservationTime = reservationTime,
            advancedCustomer = advancedCustomerDsl.toCustomer()
        )

    fun customer(initialize: AdvancedCustomerDSL.() -> Unit) {
        this.advancedCustomerDsl = AdvancedCustomerDSL().apply(initialize)
    }
}

data class AdvancedCustomer(
    val name: String,
    val numberOfAccompanies: Int
) {
    fun validateSelf() {
        if (name.isEmpty()) throw IllegalArgumentException("Name Cannot be Empty")
        if (numberOfAccompanies < 0) throw IllegalArgumentException("Number of Accompanies should be positive")
    }
}

data class AdvancedCustomerDSL(
    var name: String = "",
    var numberOfAccompanies: Int = 0
) {
    fun toCustomer() = AdvancedCustomer(name = name, numberOfAccompanies = numberOfAccompanies)
}
