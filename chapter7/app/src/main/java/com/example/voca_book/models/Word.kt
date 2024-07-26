package com.example.voca_book.models

// data class -> 상속 불가능
// toString, hashcode, equals,
data class Word(
    val text : String,
    val mean : String,
    val type : String,
)
