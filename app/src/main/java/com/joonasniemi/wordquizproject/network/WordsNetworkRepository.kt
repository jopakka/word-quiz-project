package com.joonasniemi.wordquizproject.network

class WordsNetworkRepository {
    /**
     * Fetches set of words using [WordsApi] and sets correct translations to them
     */
    suspend fun getWords(): Set<Word> {
        return setTranslations(WordsApi.retrofitService.getWords())
    }

    /**
     * Sets translations to each word
     */
    private fun setTranslations(ws: Set<Word>): Set<Word> {
        return ws.apply { forEach {
            it.addTranslations(ws.filter { w -> it.translationIds.contains(w.id) }.toSet())
        } }
    }
}