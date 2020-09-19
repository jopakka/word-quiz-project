package com.joonasniemi.wordquizproject.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://users.metropolia.fi/~joonaun/WordQuizProject/"

/**
 * Creates [Moshi] json library object
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/**
 * Creates [Retrofit] HTTP API object
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface WordsApiService {
    /**
     * Fetched [words.json] from [BASE_URL] using [retrofit] object
     */
    @GET("words.json")
    suspend fun getWords(): Set<Word>
}

object WordsApi {
    val retrofitService : WordsApiService by lazy {
        retrofit.create(WordsApiService::class.java)
    }
}