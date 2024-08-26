package com.example.githubrepositories.model

import com.google.gson.annotations.SerializedName

data class Repo(
    @SerializedName("id")
    val repoId: Long,
    @SerializedName("name")
    val repoName: String,
    @SerializedName("description")
    val repoDescription: String,
    @SerializedName("language")
    val language: String?, // nullable
    @SerializedName("stargazers_count")
    val starCount: Int,
    @SerializedName("forks_count")
    val forkCount: Int,
    @SerializedName("html_url")
    val htmlUrl: String,
)
