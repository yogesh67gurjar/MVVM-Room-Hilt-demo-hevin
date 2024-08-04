package com.yogeshandroid.practice.repository

import com.yogeshandroid.practice.model.UserResponse
import com.yogeshandroid.practice.network.ApiService
import com.yogeshandroid.practice.utils.ApiListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class MainRepository @Inject constructor(private var apiService: ApiService) {
    fun getUsers(skip: Int, limit: Int, apiListener: ApiListener) {
        apiService.getUsers(skip, limit).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        apiListener.onSuccess(it, "USERS")
                    }
                } else {
                    apiListener.onFailure(response.message())
                }
            }

            override fun onFailure(call: Call<UserResponse>, throwable: Throwable) {
                apiListener.onFailure(throwable.message.toString())
            }
        })
    }
}