package com.example.part2.newsapp.Service

import com.google.gson.GsonBuilder
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConstant {
    private const val BASE_URL = "https://news.google.com/"
    val gson = GsonBuilder().setLenient().create()
    val httpClient = OkHttpClient.Builder()
    val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(TikXmlConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(httpClient.build()).build()
}