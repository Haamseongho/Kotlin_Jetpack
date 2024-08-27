package com.example.githubrepositories.service

import com.example.githubrepositories.ApiClient
import com.example.githubrepositories.service.proxy.ApiProxy
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Network() {
    private var apiProxy: ApiProxy? = null

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
        apiProxy = ApiProxy(
            ApiClient.retrofit)
    }

    fun getApiProxy(): ApiProxy? {
        return apiProxy
    }

}