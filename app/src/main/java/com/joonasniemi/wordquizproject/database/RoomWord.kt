package com.joonasniemi.wordquizproject.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words_table", primaryKeys = ["id", "to_lang"])
data class RoomWord(
    val id: Int,
    val text: String,
    val detail: String?,
    @ColumnInfo(name = "to_lang") val toLang: String,
    @ColumnInfo(name = "times_guessed") var timesGuessed: Int,
    @ColumnInfo(name = "right_guesses") var rightGuesses: Int
)