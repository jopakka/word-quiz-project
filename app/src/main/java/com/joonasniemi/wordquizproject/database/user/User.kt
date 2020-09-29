/*
 * Joonas Niemi
 * 1908997
 */

package com.joonasniemi.wordquizproject.database.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    val language: String,
    @ColumnInfo(name = "answer_language") val answerLanguage: String,
    @ColumnInfo(name = "total_guesses") val totalGuesses: Int = 0,
    @ColumnInfo(name = "right_guesses") val rightGuesses: Int = 0,
    @PrimaryKey val id: Int = 1
)