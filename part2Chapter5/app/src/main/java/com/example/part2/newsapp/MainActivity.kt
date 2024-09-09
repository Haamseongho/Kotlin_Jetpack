package com.example.part2.newsapp

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.part2.newsapp.Service.Network
import com.example.part2.newsapp.Service.Service
import com.example.part2.newsapp.adapter.NewsAdapter
import com.example.part2.newsapp.certification.SelfSigningHelper
import com.example.part2.newsapp.databinding.ActivityMainBinding
import com.example.part2.newsapp.model.NewsRss
import com.example.part2.newsapp.model.XmlToJsonConverter
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import javax.net.ssl.X509TrustManager

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var newsAdapter: NewsAdapter
    private var selfSigningHelper = SelfSigningHelper(this)
    private var network: Network? = null
    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        network = Network.getInstance()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        network!!.getService().mainFeed().enqueue(object :Callback<NewsRss>{
            override fun onResponse(call: Call<NewsRss>, response: Response<NewsRss>) {
                if(response.isSuccessful){
                    val xmlString = response.body().toString()
                    val jsonObject = XmlToJsonConverter.convertXmlToJson(xmlString)
                    println(jsonObject.toString())
                    Log.d(TAG, jsonObject.toString())
                }
            }

            override fun onFailure(call: Call<NewsRss>, t: Throwable) {
                Log.e(TAG, t.toString())
                t.printStackTrace()
            }

        })


        initRecyclerView()
    }

    private fun initRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.newsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsAdapter
        }

        network!!.getService().mainFeed().enqueue(object :Callback<NewsRss>{
            override fun onResponse(call: Call<NewsRss>, response: Response<NewsRss>) {
                if(response.isSuccessful){
                    val xmlString = response.body().toString()
                    Log.d(TAG, xmlString)
                    val jsonObject = XmlToJsonConverter.convertXmlToJson(xmlString)
                    println(jsonObject.toString())
                    Log.d(TAG, jsonObject.toString())
                }
            }

            override fun onFailure(call: Call<NewsRss>, t: Throwable) {
                Log.e(TAG, t.toString())
                t.printStackTrace()
            }

        })
        // newItems를 넣어줘야함
//        retrofit.create(Service::class.java).mainFeed().enqueue(object : Callback<NewsRss> {
//            override fun onResponse(call: Call<NewsRss>, response: Response<NewsRss>) {
//                if (response.isSuccessful) {
//                    Log.d(TAG, response.body().toString())
//                    newsAdapter.submitList(response.body()?.channel?.items.orEmpty())
//                }
//            }
//
//            override fun onFailure(call: Call<NewsRss>, t: Throwable) {
//                Log.e(TAG, t.toString() + "/" + t.printStackTrace())
//            }
//        })
    }
}