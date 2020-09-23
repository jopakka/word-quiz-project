package com.joonasniemi.wordquizproject.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.joonasniemi.wordquizproject.network.Word

@Dao
interface WordDatabaseDao {

    @Insert
    suspend fun insert(word: RoomWord)

    @Query("SELECT * FROM words_table WHERE id = :key AND to_lang = :toLang")
    suspend fun get(key: Int, toLang: String): RoomWord?

    @Query("SELECT * FROM words_table WHERE to_lang = :toLang ORDER BY id ASC")
    fun getAll(toLang: String): LiveData<List<RoomWord>>

    @Query("DELETE FROM words_table")
    suspend fun clear()

    @Query("UPDATE words_table SET times_guessed = times_guessed + 1 WHERE id = :key AND to_lang = :toLang")
    suspend fun updateTimesGuessed(key: Int, toLang: String)

    @Query("UPDATE words_table SET right_guesses = right_guesses + 1 WHERE id = :key AND to_lang = :toLang")
    suspend fun updateRightGuessed(key: Int, toLang: String)
}