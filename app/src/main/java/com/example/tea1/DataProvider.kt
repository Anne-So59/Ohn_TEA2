package com.example.tea1

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DataProvider {
    private const val BASE_URL = "http://tomnab.fr/todo-api/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}