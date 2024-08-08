package com.example.diary.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.diary.databinding.ItemMediaManagementBinding
import com.example.diary.models.MediaContents

class MediaAdapter(private val list: MutableList<MediaContents>, val itemClickListener: ItemClickListener) :
    RecyclerView.Adapter<MediaViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = ItemMediaManagementBinding.inflate(inflater)
        return MediaViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        holder.bind(list[position], itemClickListener)
    }

    interface ItemClickListener {
        fun onItemClick(adapterPosition: Int)
    }
}

class MediaViewHolder(private val binding: ItemMediaManagementBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: MediaContents, itemClickListener: MediaAdapter.ItemClickListener) {
        binding.itemMediaImage.setImageResource(
            item.infoUri
        )
        binding.itemMediaText.text = item.infoString
        itemView.setOnClickListener { itemClickListener.onItemClick(adapterPosition) }
    }

}

