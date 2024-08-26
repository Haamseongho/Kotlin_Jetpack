package com.example.githubrepositories.service

import com.example.githubrepositories.model.Repo
import com.example.githubrepositories.model.UserDTO
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface Service {

    // Header추가
  //  @Headers("Authorization: Bearer 토큰값")
    @GET("users/{username}/repos")
    fun listRepos(@Path("username") username:String) : Call<List<Repo>>

    @GET("search/users")
    fun searchUsers(@Query("q") query: String) : Call<UserDTO>
}