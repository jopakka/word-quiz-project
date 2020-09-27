package com.joonasniemi.wordquizproject.database.words

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WordDatabaseDao {

    @Insert
    suspend fun insert(word: RoomWord)

    @Query("SELECT * FROM words_table WHERE id = :key AND language = :language AND answer_language = :answerLanguage")
    suspend fun get(key: Int, language: String, answerLanguage: String): RoomWord?

    @Query("SELECT * FROM words_table WHERE language = :language AND answer_language = :answerLanguage")
    suspend fun getAll(language: String, answerLanguage: String): List<RoomWord>

    @Query("DELETE FROM words_table")
    suspend fun clear()

    @Query("UPDATE words_table SET times_guessed = times_guessed + 1 WHERE id = :key AND language = :language AND answer_language = :answerLanguage")
    suspend fun updateTimesGuessed(key: Int, language: String, answerLanguage: String)

    @Query("UPDATE words_table SET right_guesses = right_guesses + 1 WHERE id = :key AND language = :language AND answer_language = :answerLanguage")
    suspend fun updateRightGuessed(key: Int, language: String, answerLanguage: String)
}