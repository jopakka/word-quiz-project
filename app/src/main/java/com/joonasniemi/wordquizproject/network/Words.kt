package com.joonasniemi.wordquizproject.network

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Word(
    val lang: String,
    @Json(name = "word") val text: String,
    val wiki: String,
    val imgUrl: String,
    val translations: Set<Map<String, Map<String, Set<String>>>>

): Parcelable {
    //val translations = mutableSetOf<Word>()

    /*fun addTranslation(t: Word) {
        translations.add(t)
    }

    fun addTranslations(ts: Set<Word>) {
        translations.addAll(ts)
    }*/

    fun isTranslation(word: Word): Boolean {
        return translations.contains(word)
    }

    fun translationCount(lang: String): Int {
        return translations.filter { set ->
            set.containsKey(lang)
        }.count()
    }

    // Edit distance with Wagner-Fischer algorithm
    // See https://en.wikipedia.org/wiki/Wagner%E2%80%93Fischer_algorithm
    fun editDistance(another: Word): Int {
        val m = this.text.length
        val n = another.text.length

        val d: Array<IntArray> = Array(m + 1) { IntArray(n + 1) { 0 } } // set all (m+1) * (n+1) elements to zero

        // In first element of each row increment value
        for (i in 1..m)
            d[i][0] = i

        // In first row increment value of each elements
        for (i in 1..n)
            d[0][i] = i

        for (j in 1..n) {
            for (i in 1..m) {
                val cost = if (this.text[i - 1] == another.text[j - 1]) 0 else 1

                // Checks if it's cheapest to delete, insert or replace char
                d[i][j] = minOf(d[i][j - 1] + 1,  // replace
                    d[i - 1][j] + 1,          // insert
                    d[i - 1][j - 1] + cost)     // replace
            }
        }

        return d[m][n]
    }
}