package com.hardcoreamature.chefiq

data class Ingredient(
    var id: String = "", // Default to an empty string if not provided.
    val name: String,
    var amount: Int
    // Other properties if needed
)
