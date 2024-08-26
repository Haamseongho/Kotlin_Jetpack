package com.example.githubrepositories.service

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
        val httpClient = OkHttpClient.Builder()
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder().baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient.build()).build()

        apiProxy = ApiProxy(retrofit)
    }

    fun getApiProxy(): ApiProxy? {
        return apiProxy
    }

}