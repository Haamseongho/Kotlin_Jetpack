package com.example.voca_book.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.parcelize.Parcelize

// data class -> 상속 불가능
// toString, hashcode, equals,

@Parcelize
@Entity(tableName = "word")
data class Word(
    val text : String,
    val mean : String,
    val type : String,
    // 주된 키
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // 자동생성 -> id

) : Parcelable  // kotlin-parcelize : 직렬, 역직렬화
