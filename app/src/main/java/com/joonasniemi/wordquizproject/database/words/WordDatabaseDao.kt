/*
 * Joonas Niemi
 * 1909887
 */

package com.joonasniemi.wordquizproject.database.words

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

/**
 * Data access object for WordDatabase
 */
@Dao
interface WordDatabaseDao {

    /**
     * Inserts new [word] to wordDatabase
     */
    @Insert
    suspend fun insert(word: RoomWord)

    /**
     * Selects word from wordDatabase which id is [key] and
     * language is [language] and answer_language is [answerLanguage]
     */
    @Query("SELECT * FROM words_table WHERE id = :key AND language = :language AND answer_language = :answerLanguage")
    suspend fun get(key: Int, language: String, answerLanguage: String): RoomWord?

    /**
     * Select all words which language is [language] and answer_language is [answerLanguage]
     */
    @Query("SELECT * FROM words_table WHERE language = :language AND answer_language = :answerLanguage")
    suspend fun getAll(language: String, answerLanguage: String): List<RoomWord>

    /**
     * Clear whole wordDatabase
     */
    @Query("DELETE FROM words_table")
    suspend fun clear()

    /**
     * Updates selected word times_guessed value by 1
     */
    @Query("UPDATE words_table SET times_guessed = times_guessed + 1 WHERE id = :key AND language = :language AND answer_language = :answerLanguage")
    suspend fun updateTimesGuessed(key: Int, language: String, answerLanguage: String)

    /**
     * Updates selected word right_guesses value by 1
     */
    @Query("UPDATE words_table SET right_guesses = right_guesses + 1 WHERE id = :key AND language = :language AND answer_language = :answerLanguage")
    suspend fun updateRightGuessed(key: Int, language: String, answerLanguage: String)
}