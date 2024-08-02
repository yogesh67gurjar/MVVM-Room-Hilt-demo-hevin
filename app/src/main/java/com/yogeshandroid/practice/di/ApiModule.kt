package com.yogeshandroid.practice.di

import com.yogeshandroid.practice.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    @Named("BASE_URL")
    fun getBaseUrl() = "https://dummyjson.com/"

    @Provides
    fun getRetrofitInstance(@Named("BASE_URL") baseUrl: String): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    fun getApiServiceInstance(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}