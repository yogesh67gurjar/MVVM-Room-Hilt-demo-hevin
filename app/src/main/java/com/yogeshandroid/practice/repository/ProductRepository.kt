package com.yogeshandroid.practice.repository

import com.yogeshandroid.practice.model.ProductResponse
import com.yogeshandroid.practice.model.UserResponse
import com.yogeshandroid.practice.network.ApiService
import com.yogeshandroid.practice.utils.ApiListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ProductRepository @Inject constructor(private var apiService: ApiService) {
    fun getProducts(skip: Int, limit: Int, apiListener: ApiListener) {
        apiService.getProducts(skip, limit).enqueue(object : Callback<ProductResponse> {
            override fun onResponse(
                call: Call<ProductResponse>,
                response: Response<ProductResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        apiListener.onSuccess(it, "PRODUCTS")
                    }
                } else {
                    apiListener.onFailure(response.message())
                }
            }

            override fun onFailure(call: Call<ProductResponse>, throwable: Throwable) {
                apiListener.onFailure(throwable.message.toString())
            }
        })
    }
}