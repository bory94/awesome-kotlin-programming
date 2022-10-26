package com.bory.awesomekotlin.dsl.intermediate

data class IntermediateCustomer(
    val name: String,
    val numberOfAccompanies: Int
) {
    fun validateSelf() {
        if (name.isEmpty()) throw IllegalArgumentException("Name Cannot be Empty")
        if (numberOfAccompanies < 0) throw IllegalArgumentException("Number of Accompanies should be positive")
    }
}

data class IntermediateCustomerDSL(
    var name: String = "",
    var numberOfAccompanies: Int = 0
) {
    fun toCustomer() = IntermediateCustomer(name = name, numberOfAccompanies = numberOfAccompanies)
}