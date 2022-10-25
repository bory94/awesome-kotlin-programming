package com.bory.awesomekotlin.dsl.basic

import java.time.LocalDateTime

class ReservationManager {
    fun handlerReservationRequest(initialize: Reservation.() -> Unit) =
        Reservation().apply {
            initialize()
            validateSelf()
        }
}

fun main() {
    val manager = ReservationManager()

    val reservation = manager.handlerReservationRequest {
        reservationTime = LocalDateTime.of(2023, 10, 15, 22, 0, 0)

        customer {
            name = "John Doe"
            numberOfAccompanies = 2
        }
    }

    println(reservation)
}