package com.bory.awesomekotlin.dsl.basic

import java.time.LocalDateTime

class SimpleReservationManager {
    infix fun reserve(initialize: SimpleReservation.() -> Unit) =
        SimpleReservation().apply {
            initialize()
            validateSelf()
        }
}

fun main() {
    val reservation = SimpleReservationManager() reserve {
        customerName = "Jane Doe"
        numberOfAccompanies = 2
        reservationTime = LocalDateTime.of(2022, 11, 1, 17, 0, 30)
    }

    println(reservation)
}