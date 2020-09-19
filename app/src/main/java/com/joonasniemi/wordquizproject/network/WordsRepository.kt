package com.joonasniemi.wordquizproject.network

class WordsRepository {
    companion object {
        private const val TAG = "WordsRepository"
    }

    suspend fun getWords(): Set<Word> {
        return setTranslations(WordsApi.retrofitService.getWords())
    }

    private fun setTranslations(ws: Set<Word>): Set<Word> {
        return ws.apply { forEach {
            it.addTranslations(ws.filter { w -> it.translationIds.contains(w.id) }.toSet())
        } }
    }
}