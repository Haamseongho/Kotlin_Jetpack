package com.example.githubrepositories.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.githubrepositories.R
import com.example.githubrepositories.databinding.ItemRepoBinding
import com.example.githubrepositories.model.Repo

// onClick의 형 = Repo
class RepoAdapter(private val onClick: (Repo) -> Unit) : ListAdapter<Repo, RepoAdapter.RepoAdapterViewHolder>(diffUtil) {

    companion object {
        // ItemCallbackDms 인터페이스니까 object로 하기
        val diffUtil = object : DiffUtil.ItemCallback<Repo>() {
            override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean {
                return oldItem.repoId == newItem.repoId
            }

            override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class RepoAdapterViewHolder(private val binding: ItemRepoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Repo) {
            binding.repoNameTextView.text = item.repoName
            binding.descriptionTextView.text = item.repoDescription
            binding.forkCountTextView.text = item.forkCount.toString()
            binding.starCountTextView.text = item.starCount.toString()

            binding.root.setOnClickListener {
                onClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoAdapterViewHolder {
        return RepoAdapterViewHolder(
            binding = ItemRepoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RepoAdapterViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}