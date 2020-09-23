package com.joonasniemi.wordquizproject.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.joonasniemi.wordquizproject.network.Word

@Database(entities = [RoomWord::class], version = 5, exportSchema = false)
abstract class WordDatabase : RoomDatabase() {
    abstract val wordDatabaseDao: WordDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: WordDatabase? = null

        fun getInstance(context: Context): WordDatabase{
            synchronized(this){
                var instance = INSTANCE
                if(instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        WordDatabase::class.java,
                        "guessed_words_database"
                    ).fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}