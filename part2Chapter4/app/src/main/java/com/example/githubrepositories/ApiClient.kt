package com.example.githubrepositories

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://api.github.com/"
//    val httpClient = OkHttpClient.Builder().addInterceptor {
//        val request = it.request().newBuilder()
//            .addHeader("Authorization", "auth_value")
//            .build()
//        it.proceed(request)
//    } // OKHttpClient에 addInterceptor를 활용해서 헤더를 더할 수 있음
      // interceptor.Chain -> request에 newBuilder를 만드는데 헤더를 달아서 빌드하고
      // 실행시키도록 proceed를 하여 선언한 변수를 넣어주기
    val httpClient = OkHttpClient.Builder()
    val gson = GsonBuilder().setLenient().create()
    val retrofit: Retrofit = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(httpClient.build()).build()


}