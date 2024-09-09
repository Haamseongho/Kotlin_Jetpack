package com.example.githubrepositories.service

import com.example.githubrepositories.ApiClient
import com.example.githubrepositories.service.proxy.ApiProxy
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class Network() {
    private var service: Service? = null
    companion object {
        @Volatile
        private var instance: Network? = null

        fun getInstance(): Network {
            return instance ?: synchronized(this) {
                instance ?: Network().also { instance = it }
            }
        }
    }

    init {
        service = ApiClient.retrofit.create(Service::class.java)
    }
    fun getService(): Service{
        return service!!
    }
}