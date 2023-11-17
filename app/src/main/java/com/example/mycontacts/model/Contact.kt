package com.example.mycontacts.model

data class Contact(
    val name: String,
    val paternalSurname: String,
    val maternalSurname: String,
    val age: Int,
    val number: Int,
    val gender: String,
    val imageUrl: String,
    val email: String
)
