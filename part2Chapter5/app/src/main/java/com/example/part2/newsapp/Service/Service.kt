package com.example.part2.newsapp.Service

import com.example.part2.newsapp.model.NewsRss
import retrofit2.Call
import retrofit2.http.GET

interface Service {
    @GET("/rss")
    fun mainFeed(): Call<NewsRss>
}