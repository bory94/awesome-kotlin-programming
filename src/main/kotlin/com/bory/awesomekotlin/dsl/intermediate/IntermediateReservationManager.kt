package com.bory.awesomekotlin.dsl.intermediate

import java.time.LocalDateTime
import java.util.*

class IntermediateReservationManager {
    infix fun reserve(initialize: IntermediateReservationDSL.() -> Unit) =
        IntermediateReservationDSL().apply(initialize).toReservation().apply { validateSelf() }
}

fun main() {
    val reservation = IntermediateReservationManager() reserve {
        reservationTime = LocalDateTime.of(2023, 10, 15, 22, 0, 0)

        customer {
            name = "John Doe"
            numberOfAccompanies = 2
        }
    }

    println(reservation)

    val reservation2 = IntermediateReservation(
        id = UUID.randomUUID(),
        reservationTime = LocalDateTime.of(2023, 10, 15, 22, 0, 0),
        intermediateCustomer = IntermediateCustomer(
            name = "Jane Doe",
            numberOfAccompanies = 3
        )
    )
    reservation2.validateSelf()

    println(reservation2)
}