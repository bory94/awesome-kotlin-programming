package com.bory.awesomekotlin.dsl.basic

import java.time.LocalDateTime

class ReservationManager {
    infix fun reserve(initialize: ReservationDSL.() -> Unit) =
        ReservationDSL().apply(initialize).toReservation().apply { validateSelf() }
}

fun main() {
    val reservation = ReservationManager() reserve {
        reservationTime = LocalDateTime.of(2023, 10, 15, 22, 0, 0)

        customer {
            name = "John Doe"
            numberOfAccompanies = 2
        }
    }

    println(reservation)
}