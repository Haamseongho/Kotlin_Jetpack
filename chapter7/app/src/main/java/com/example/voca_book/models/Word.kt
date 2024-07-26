package com.example.voca_book.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query

// data class -> 상속 불가능
// toString, hashcode, equals,

@Entity(tableName = "word")
data class Word(
    val text : String,
    val mean : String,
    val type : String,
    // 주된 키
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // 자동생성 -> id

)
