/*
 * Joonas Niemi
 * 1909887
 */

package com.joonasniemi.wordquizproject.database.words

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RoomWord::class], version = 8, exportSchema = false)
abstract class WordDatabase : RoomDatabase() {
    abstract val wordDatabaseDao: WordDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: WordDatabase? = null

        /**
         * Returns current instance or gets new, if there isn't any
         */
        fun getInstance(context: Context): WordDatabase {
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