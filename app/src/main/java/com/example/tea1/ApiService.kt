package com.example.tea1

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("authenticate")
    fun authenticateUser(
        @Query("user") user: String,
        @Query("password") password: String
    ): Call<AuthenticateDataClass>

    @GET("lists")
    fun getLists(
        @Header("hash") hash: String
    ): Call<ListDataClass>

    @GET("lists/{id}/items")
    fun getItems(
        @Path("id") listId: String,
        @Header("hash") hash: String
    ): Call<ItemDataClass>

    @POST("lists")
    fun ajoutList(
        @Header("hash") hash: String,
        @Query("label") label: String
    ): Call<NewListDataClass>

    @POST("lists/{id}/items")
    fun ajoutItem(
        @Path("id") listId: String,
        @Query("label") label: String,
        @Header("hash") hash: String
    ): Call<NewItemDataClass>

}