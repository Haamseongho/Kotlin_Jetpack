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

    companion object {
        var hasMore: Boolean? = false
    }

    init {
        service = retrofit.create(Service::class.java)
    }

    // MainActivity -> Repository api 가져오기
    fun getApiDataFromGithub(
        context: Context,
        username: String,
        repoAdapter: RepoAdapter,
        page: Int
    ) {
        service?.listRepos(username, page)?.enqueue(object : Callback<List<Repo>> {
            override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
                Log.d("MainActivity", response.body().toString())
                hasMore = response.body()?.count() == 30 // per_page = 30이므로 더 있음을 확인하고 페이지를 넘김
                // 현재 리스트 + 응답으로 오는 리스트 (+ 만써도 더해짐) 응답 없는경우 생각해서 .orEmpty() 두기
                repoAdapter.submitList(repoAdapter.currentList + response.body().orEmpty())  // 새로운것만 넣는 것이 아니라 어댑터의 현재 리스트 + 응답 바디값
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