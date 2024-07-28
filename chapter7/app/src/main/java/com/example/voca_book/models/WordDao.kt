package com.example.voca_book.models

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface WordDao {
    // 없는 테이블이 들어가면 오류남
    @Query("SELECT * from word ORDER BY id DESC")
    fun getAll(): List<Word>

    @Query("SELECT * from word ORDER BY id DESC LIMIT 1") // 최신꺼 하나만 받기
    fun getLatestWord(): List<Word>

    @Insert
    fun insert(word: Word)

    @Delete
    fun delete(word: Word)

    @Update
    fun update(word: Word)
}