package com.joonasniemi.wordquizproject.network

class WordsRepository {
    suspend fun getWords(): Set<Word> {
        return WordsApi.retrofitService.getWords()
    }
}