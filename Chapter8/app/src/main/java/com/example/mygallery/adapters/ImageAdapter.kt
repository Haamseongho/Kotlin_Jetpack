package com.example.mygallery.adapters

import android.content.ClipData.Item
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mygallery.databinding.ItemImageBinding
import com.example.mygallery.databinding.ItemLoadMoreBinding

// 1인자 : item, 2인자 : RecyclerView.ViewHolder
class ImageAdapter(private val itemClickListener: ItemClickListener?= null) :
    ListAdapter<ImageItems, RecyclerView.ViewHolder >(object : DiffUtil.ItemCallback<ImageItems>() {

        // 같은 데이터인지
        override fun areItemsTheSame(oldItem: ImageItems, newItem: ImageItems): Boolean {
            return oldItem === newItem // kt = equal  값 같음
        }
        // 같은 데이터를 참조하는건지
        override fun areContentsTheSame(oldItem: ImageItems, newItem: ImageItems): Boolean {
            return oldItem == newItem  // == 참조
        }

    }) {
    // footer 구현
    override fun getItemCount(): Int {
        val originSize = currentList.size  // ListAdapter에서 가지고 있는 현재 리스트
        return if(originSize == 0) 0 else originSize.inc() // Footer가 추가되어서 하나 넣는 느낌
    }
    // Item이 둘 이상의 요소가 생기면서 타입도 체크해야하는 경우가 생김
    override fun getItemViewType(position: Int): Int {
        // 마지막엔 꼭 Footer
        return if(itemCount.dec() == position){
            ITEM_LOAD_MORE
        } else {
            ITEM_IMAGE
        }
       // return super.getItemViewType(position)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        return when(viewType){
            ITEM_IMAGE -> {
                val binding = ItemImageBinding.inflate(inflater, parent, false)
                ImageViewHolder(binding)
            }
            // ITEM_LOAD_MORE
            else -> {
                val binding = ItemLoadMoreBinding.inflate(inflater, parent, false)
                LoadMoreViewHolder(binding)
            }
        }
    }
    // 자식 클래스로 sealed class가 부모다보니 자식을 알아서 셋팅 알아서 함
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ImageViewHolder -> {
                holder.bind(currentList[position] as ImageItems.Image)
            }
            is LoadMoreViewHolder -> {
                itemClickListener?.let { holder.bind(it) }
            }
        }
    }

    companion object {
        const val ITEM_IMAGE = 0
        const val ITEM_LOAD_MORE = 1
    }

    interface ItemClickListener {
        fun onLoadMoreClick()
    }
}
sealed class ImageItems {
    data class Image(
        val uri: Uri,
    ) : ImageItems()

    object LoadMore : ImageItems()
}

class ImageViewHolder(private val binding: ItemImageBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(item: ImageItems.Image){
        binding.previewImageView.setImageURI(item.uri)
    }
}

class LoadMoreViewHolder(private val binding: ItemLoadMoreBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(itemClickListener: ImageAdapter.ItemClickListener){
        itemView.setOnClickListener { itemClickListener.onLoadMoreClick() }
    }
}