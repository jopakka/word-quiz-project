package com.joonasniemi.wordquizproject.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://users.metropolia.fi/~joonaun/WordQuizProject/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface WordsApiService {
    @GET("Words.json")
    suspend fun getWords(): Set<Word>
}

object WordsApi {
    val retrofitService : WordsApiService by lazy {
        retrofit.create(WordsApiService::class.java)
    }
}