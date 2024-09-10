package com.example.chatapp.chatdetail.adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.chatdetail.model.ChatItem
import com.example.chatapp.databinding.ItemChatContentBinding
import com.example.chatapp.userList.model.UserItem

class ChatAdapter : ListAdapter<ChatItem, ChatAdapter.ViewHolder>(diffUtil) {

    var otherUserItem: UserItem? = null
    val myUserItem: UserItem? = null

    inner class ViewHolder(private var binding: ItemChatContentBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(chatItem: ChatItem) {

            binding.messageTextView.text = chatItem.message  // message는 그대로 넣어주기


            if(chatItem.userId == otherUserItem?.userId){
                binding.usernameTextView.isVisible = true
                binding.usernameTextView.text = otherUserItem?.username
                binding.messageTextView.gravity = Gravity.START
            }

            else {
                binding.usernameTextView.isVisible = false
                binding.usernameTextView.text = myUserItem?.username
                binding.messageTextView.gravity = Gravity.END
            }
        }
    }
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ChatItem>() {
            override fun areItemsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
                return oldItem.chatId == newItem.chatId
            }

            override fun areContentsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
                return oldItem == newItem
            }

        }
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemChatContentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}