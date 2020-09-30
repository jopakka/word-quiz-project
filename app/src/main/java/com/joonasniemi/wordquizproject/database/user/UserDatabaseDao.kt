/*
 * Joonas Niemi
 * 1909887
 */

package com.joonasniemi.wordquizproject.database.user

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

/**
 * Data access object for UserDatabase
 */
@Dao
interface UserDatabaseDao {

    /**
     * Inserts new [user] to database
     */
    @Insert
    suspend fun insert(user: User)

    /**
     * Select that user which id is 1 and return liveData from it
     */
    @Query("SELECT * FROM user WHERE id = 1")
    fun getUser(): LiveData<User>

    /**
     * Clears whole user database
     */
    @Query("DELETE FROM user")
    suspend fun clear()

    /**
     * Increases users right_guesses value by 1
     */
    @Query("UPDATE user SET right_guesses = right_guesses + 1 WHERE id = 1")
    suspend fun updateRightGuesses()

    /**
     * Increases users total_guesses value by 1
     */
    @Query("UPDATE user SET total_guesses = total_guesses + 1 WHERE id = 1")
    suspend fun updateTotalGuesses()

    /**
     * Updates user languages to [language] and [answerLanguage]
     */
    @Query("UPDATE user SET language = :language, answer_language = :answerLanguage WHERE id = 1")
    suspend fun updateLanguages(language: String, answerLanguage: String)
}