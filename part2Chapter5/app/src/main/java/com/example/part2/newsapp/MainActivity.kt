package com.example.part2.newsapp

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.part2.newsapp.model.NewsRss
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.TikXmlConfig
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.create

class MainActivity : AppCompatActivity() {

    private val retrofit: Retrofit = Retrofit.Builder().baseUrl("https://news.google.com")
        .addConverterFactory(
            TikXmlConverterFactory.create(
                TikXml.Builder().exceptionOnUnreadXml(false).build() // 필요한 데이터만 골라서 매핑할거라 다른것들에 대해서는 그냥 넘어가도록 설정
            )
        ).build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        retrofit.create(NewsService::class.java).mainFeed().enqueue(object : Callback<NewsRss>{
            override fun onResponse(call: Call<NewsRss>, response: Response<NewsRss>) {
                if(response.isSuccessful){
                    Log.e("MainActivity", response.body()?.channel?.items.toString())
                }
            }

            override fun onFailure(call: Call<NewsRss>, response: Throwable) {
                response.printStackTrace()
            }
        })
    }
}