/*
 * Joonas Niemi
 * 1908997
 */

package com.joonasniemi.wordquizproject.database.user

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDatabaseDao {

    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM user WHERE id = 1")
    fun getUser(): LiveData<User>

    @Query("DELETE FROM user")
    suspend fun clear()

    @Query("UPDATE user SET right_guesses = right_guesses + 1 WHERE id = 1")
    suspend fun updateRightGuesses()

    @Query("UPDATE user SET total_guesses = total_guesses + 1 WHERE id = 1")
    suspend fun updateTotalGuesses()

    @Query("UPDATE user SET language = :language, answer_language = :answerLanguage WHERE id = 1")
    suspend fun updateLanguages(language: String, answerLanguage: String)
}