package com.example.diary.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.diary.databinding.ItemImagesBinding
import com.example.diary.databinding.ItemLoadMoreBinding
import com.example.diary.models.DiaryContents
import com.example.diary.models.ITEM_IMAGE
import com.example.diary.models.LOAD_MORE

class DiaryAdapter(private val itemClickListener: ItemClickListener? = null) :
    ListAdapter<DiaryContents, RecyclerView.ViewHolder>(object :
        DiffUtil.ItemCallback<DiaryContents>() {
        override fun areItemsTheSame(oldItem: DiaryContents, newItem: DiaryContents): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: DiaryContents, newItem: DiaryContents): Boolean {
            return oldItem == newItem
        }

    }) {
    interface ItemClickListener {
        fun onLoadMoreItems()
    }

    override fun getItemCount(): Int {
        val originSize = currentList.size
        return if (originSize == 0) 0 else originSize.inc()
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemCount.dec() == position) {
            LOAD_MORE
        } else {
            ITEM_IMAGE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return if (viewType == LOAD_MORE) {
            val binding = ItemLoadMoreBinding.inflate(inflater, parent, false)
            LoadMoreHolder(binding)
        } else {
            val binding = ItemImagesBinding.inflate(inflater, parent, false)
            ImageViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ImageViewHolder -> {
                holder.bind(currentList[position] as? DiaryContents.DiaryItems)
            }

            is LoadMoreHolder -> {
                itemClickListener.let { listener ->
                    listener?.let { holder.bind(it) }
                }
            }
        }
    }
}



class ImageViewHolder(private val binding: ItemImagesBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: DiaryContents.DiaryItems?) {
        binding.itemImageView.setImageURI(item?.uri)
    }
}

class LoadMoreHolder(private val binding: ItemLoadMoreBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(itemClickListener: DiaryAdapter.ItemClickListener) {
        itemView.setOnClickListener { itemClickListener.onLoadMoreItems() }
    }
}