package com.example.githubrepositories.service.proxy

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.githubrepositories.adapter.RepoAdapter
import com.example.githubrepositories.adapter.UserAdapter
import com.example.githubrepositories.model.Repo
import com.example.githubrepositories.model.UserDTO
import com.example.githubrepositories.service.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class ApiProxy(retrofit: Retrofit) {
    private var service: Service? = null

    init {
        service = retrofit.create(Service::class.java)
    }

    // MainActivity -> Repository api 가져오기
    fun getApiDataFromGithub(context: Context, username: String, repoAdapter: RepoAdapter) {
        service?.listRepos(username)?.enqueue(object : Callback<List<Repo>> {
            override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
                Log.d("MainActivity", response.body().toString())
                repoAdapter.submitList(response.body())
            }

            override fun onFailure(call: Call<List<Repo>>, response: Throwable) {
                Toast.makeText(context, "데이터 가져오기 실패", Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun searchUsers(context: Context, query: String, userAdapter: UserAdapter) {
        service?.searchUsers(query)?.enqueue(object : Callback<UserDTO> {
            override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                if (response.isSuccessful) {
                    Log.e("MainActivity", response.body().toString())
                    userAdapter.submitList(response.body()?.items) // 비어있으면 공백으로 들어감
                }
            }

            override fun onFailure(call: Call<UserDTO>, response: Throwable) {
                Toast.makeText(context, "데이터 가져오기 실패", Toast.LENGTH_SHORT).show()
            }

        })
    }
}