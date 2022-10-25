package com.bory.awesomekotlin.dsl.basic

import java.time.LocalDateTime
import java.util.*

data class SimpleReservation(
    var id: UUID = UUID.randomUUID(),
    var customerName: String = "",
    var numberOfAccompanies: Int = 0,
    var reservationTime: LocalDateTime = LocalDateTime.now()
) {
    fun validateSelf() {
        if (customerName.isEmpty()) throw IllegalArgumentException("Name Cannot be Empty")
        if (numberOfAccompanies < 0) throw IllegalArgumentException("Number of Accompanies should be positive")
        if (reservationTime < LocalDateTime.now()) throw IllegalArgumentException("Reservation Time should be after now")
    }
}