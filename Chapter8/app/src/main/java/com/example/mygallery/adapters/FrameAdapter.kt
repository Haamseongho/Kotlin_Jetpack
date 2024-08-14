package com.example.mygallery.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mygallery.databinding.ItemFrameBinding
import com.example.mygallery.frames.FrameActivity

class FrameAdapter(private val list: List<FrameItem.ImageSubItems>) : RecyclerView.Adapter<FrameViewHolder>() {

    companion object {
        const val FRAME_IMAGE = 102
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FrameViewHolder {
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = ItemFrameBinding.inflate(inflater, parent,false)
        return FrameViewHolder(binding)

    }

    override fun onBindViewHolder(holder: FrameViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

class FrameViewHolder(private val binding: ItemFrameBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(item: FrameItem.ImageSubItems){
        binding.frameImageView.setImageURI(item.uri)
    }
}

sealed class FrameItem {
    data class ImageSubItems (
        val uri: Uri
    )
}