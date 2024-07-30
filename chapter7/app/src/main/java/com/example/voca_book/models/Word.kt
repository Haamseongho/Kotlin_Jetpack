package com.example.voca_book.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.parcelize.Parceler
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

) : Parcelable {
    companion object : Parceler<Word> {
        override fun create(parcel: Parcel): Word {
            val text = parcel.readString() ?: ""
            val mean = parcel.readString() ?: ""
            val type = parcel.readString() ?: ""
            val id = parcel.readInt()
            return Word(text, mean, type, id)
        }

        override fun Word.write(parcel: Parcel, flags: Int) {
            parcel.writeString(text)
            parcel.writeString(mean)
            parcel.writeString(type)
            parcel.writeInt(id)
        }

    }
} // kotlin-parcelize : 직렬, 역직렬화
