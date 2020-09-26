package com.joonasniemi.wordquizproject.database.words

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WordDatabaseDao {

    @Insert
    suspend fun insert(word: RoomWord)

    @Query("SELECT * FROM words_table WHERE id = :key AND answer_language = :answerLanguage")
    suspend fun get(key: Int, answerLanguage: String): RoomWord?

    @Query("SELECT * FROM words_table WHERE answer_language = :answerLanguage ORDER BY id ASC")
    fun getAll(answerLanguage: String): LiveData<List<RoomWord>>

    @Query("DELETE FROM words_table")
    suspend fun clear()

    @Query("UPDATE words_table SET times_guessed = times_guessed + 1 WHERE id = :key AND answer_language = :answerLanguage")
    suspend fun updateTimesGuessed(key: Int, answerLanguage: String)

    @Query("UPDATE words_table SET right_guesses = right_guesses + 1 WHERE id = :key AND answer_language = :answerLanguage")
    suspend fun updateRightGuessed(key: Int, answerLanguage: String)
}