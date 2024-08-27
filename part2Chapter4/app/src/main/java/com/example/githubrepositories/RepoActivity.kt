package com.example.githubrepositories

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubrepositories.adapter.RepoAdapter
import com.example.githubrepositories.databinding.ActivityRepoBinding
import com.example.githubrepositories.service.Network
import com.example.githubrepositories.service.proxy.ApiProxy

class RepoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRepoBinding
    private lateinit var network: Network
    private var page: Int = 0
    private var username: String? = null
    private var hasMore = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRepoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // 선택했을때 일어나는 콜백 여기다 구현
        val repoAdapter = RepoAdapter { // item이 it으로 넘어올 것이고
            // 해당 item의 요소들을 접근할 수 있음
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.htmlUrl))
            // 내장브라우저 열기
            startActivity(intent)
        }
        val linearLayoutManager = LinearLayoutManager(this@RepoActivity)
        username = intent?.getStringExtra("username")


        binding.repoRecyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = repoAdapter
        }
        // 스크롤 이벤트
        binding.repoRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalCount = linearLayoutManager.itemCount
                val lastVisiblePosition =
                    linearLayoutManager.findLastCompletelyVisibleItemPosition()
                // lastVisiblePosition은 마지막 보이는 위치 이고 이것이 totalCount보다 크거나 같은 경우 = 마지막 페이지
                if (lastVisiblePosition >= (totalCount - 1) && ApiProxy.hasMore == true) {
                    page += 1
                    listRepo(repoAdapter, page)
                }
            }
        })

        // 넘어왔을때 자동으로 보여줌
        listRepo(repoAdapter, 0)
    }

    private fun listRepo(repoAdapter: RepoAdapter, page: Int) {
        network = Network.getInstance()
        binding.userNameTextView.text = username
        network.getApiProxy()
            ?.getApiDataFromGithub(this@RepoActivity, username ?: "", repoAdapter, page)
    }
}