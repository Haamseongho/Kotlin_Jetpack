package com.example.githubrepositories.adapter

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.githubrepositories.databinding.ItemUserBinding
import com.example.githubrepositories.model.User

class UserAdapter(val onClick: (User) -> Unit) :
    ListAdapter<User, UserAdapter.UserAdapterViewHolder>(diffUtil) {

    companion object {
        // DiffUtl.ItemCallback -> ListAdapter에 인자로 사용
        // 기존꺼, 새로운거 같은 값인지 확인
        val diffUtil = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.id == newItem.id
            }

            // data class이기 때문에 이 안에서는 equals, toString 모두 다 내재되어 구현되어 있으므로
            // 객체 자체로 비교해도 됨
            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class UserAdapterViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: User) {
            binding.usernameTextView.text = item.username
            binding.root.setOnClickListener {
                onClick(item) // onClick -> User 넘겨주기
            }
        }
    }

    // ViewHolder를 리턴해주는데,
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapterViewHolder {
        return UserAdapterViewHolder(
            binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
//        val binding: I
//        val layoutInflater =
//            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        val binding: ItemUserBinding = ItemUserBinding.inflate(layoutInflater, parent, false)
//        return UserAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserAdapterViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}