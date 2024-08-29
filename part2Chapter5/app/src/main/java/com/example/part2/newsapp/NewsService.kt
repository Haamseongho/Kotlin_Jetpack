package com.example.part2.newsapp

import com.example.part2.newsapp.model.NewsRss
import retrofit2.Call
import retrofit2.http.GET

interface NewsService {
    @GET("rss?hl=ko&gl=KR&ceid=KR:ko")
    fun mainFeed(): Call<NewsRss>
}