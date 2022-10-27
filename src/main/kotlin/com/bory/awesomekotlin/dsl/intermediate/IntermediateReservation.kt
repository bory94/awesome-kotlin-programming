package com.bory.awesomekotlin.dsl.intermediate

import java.time.LocalDateTime
import java.util.*

data class IntermediateReservation(
    val id: UUID,
    val reservationTime: LocalDateTime,
    val intermediateCustomer: IntermediateCustomer
) {
    fun validateSelf() {
        if (reservationTime < LocalDateTime.now()) throw IllegalArgumentException("Reservation Time should be after now")
        this.intermediateCustomer.validateSelf()
    }
}

data class IntermediateReservationDSL(
    val id: UUID = UUID.randomUUID(),
    var reservationTime: LocalDateTime = LocalDateTime.now(),
    var intermediateCustomerDsl: IntermediateCustomerDSL = IntermediateCustomerDSL()
) {
    fun toReservation() =
        IntermediateReservation(
            id = id,
            reservationTime = reservationTime,
            intermediateCustomer = intermediateCustomerDsl.toCustomer()
        )

    inline fun customer(initialize: IntermediateCustomerDSL.() -> Unit) {
        this.intermediateCustomerDsl = IntermediateCustomerDSL().apply(initialize)
    }
}

