package com.example.lab2.domain

const val HORSE_ID: String = "HORSE_ID"

data class Horse (
    var id: Int = -1,
    var name: String = "",
    var breed: String = "",
    var gender: String = "",
    var colour: String = "",
    var age: Int = -1
)