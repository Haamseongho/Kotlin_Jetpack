package com.example.chatapp.chatList.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.chatList.model.ChatRoomItem
import com.example.chatapp.databinding.ItemChatBinding

class ChatAdapter : ListAdapter<ChatRoomItem, ChatAdapter.ViewHolder>(diff) {

    inner class ViewHolder(private val binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(chatItem: ChatRoomItem){
            binding.nicknameTextView.text = chatItem.otherUserName
            // binding.profileImageView.setImageResource(chatItem.) // 이미지 넣는거 할 예정
            binding.lastMessageTextView.text = chatItem.lastMessage
        }
    }

    companion object {
        val diff = object : DiffUtil.ItemCallback<ChatRoomItem>() {
            override fun areItemsTheSame(oldItem: ChatRoomItem, newItem: ChatRoomItem): Boolean {
                return oldItem.chatRoomId == newItem.chatRoomId
            }

            override fun areContentsTheSame(oldItem: ChatRoomItem, newItem: ChatRoomItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemChatBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}