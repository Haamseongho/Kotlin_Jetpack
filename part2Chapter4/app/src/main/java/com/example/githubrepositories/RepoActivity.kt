package com.example.githubrepositories

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubrepositories.adapter.RepoAdapter
import com.example.githubrepositories.databinding.ActivityRepoBinding
import com.example.githubrepositories.service.Network

class RepoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRepoBinding
    private lateinit var network: Network

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
        val repoAdapter = RepoAdapter()

        binding.repoRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@RepoActivity)
            adapter = repoAdapter
        }
        // 넘어왔을때 자동으로 보여줌
        listRepo(repoAdapter)
    }

    private fun listRepo(repoAdapter: RepoAdapter) {
        network = Network.getInstance()
        val username = intent?.getStringExtra("username")
        binding.userNameTextView.text = username
        network.getApiProxy()?.getApiDataFromGithub(this@RepoActivity, username ?: "", repoAdapter)
    }
}