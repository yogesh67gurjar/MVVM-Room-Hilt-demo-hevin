package com.yogeshandroid.practice.network

import com.yogeshandroid.practice.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    fun getUsers(@Query("skip") skip: Int,@Query("limit") limit: Int): Call<UserResponse>
}