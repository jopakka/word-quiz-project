/*
 * Joonas Niemi
 * 1908997
 */

package com.joonasniemi.wordquizproject.database.words

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "words_table", primaryKeys = ["id", "language", "answer_language"])
data class RoomWord(
    val id: Int,
    val text: String,
    val detail: String?,
    val language: String,
    @ColumnInfo(name = "answer_language") val answerLanguage: String,
    @ColumnInfo(name = "times_guessed") var timesGuessed: Int,
    @ColumnInfo(name = "right_guesses") var rightGuesses: Int
)