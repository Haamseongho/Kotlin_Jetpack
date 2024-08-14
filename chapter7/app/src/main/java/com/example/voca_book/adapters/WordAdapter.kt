package com.example.voca_book.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.voca_book.databinding.WordItemBinding
import com.example.voca_book.models.Word

// ItemClickListener 추가
class WordAdapter(
    val list: MutableList<Word>,
    private val itemClickListener: ItemClickListener? = null
): RecyclerView.Adapter<WordAdapter.ViewHolder>() {

    // onBindViewHolder에 binding으로 접근하기 위해서 binding을 뷰홀더에 넣고 뷰홀더 부분에서는 item.xml 바인딩을 두어
    // holder에서도 바인딩을 접근할 수 있게 만듬
    class ViewHolder(private val binding: WordItemBinding) : RecyclerView.ViewHolder(binding.root){
        // onBindViewHolder의 역할은 데이터와 뷰의 바인딩 작업을 하는건데 이걸 ViewHolder 클래스에서 bind라는 함수를 만들어서
        // 구현할 수 있음
        fun bind(word: Word){
            binding.apply {
                textTextView.text = word.text
                meanTextView.text = word.mean
                typeChip.text = word.type
            }
            // itemView는 뷰홀더에서 들어온 아이템 뷰 들을 바로 접근할 수 있는 인자
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // viewHolder만들기
        // item.xml 연결하기
        // ViewHolder를 생성하는데 필요한 것
        /*
        1) parent.context.getSystemService에서 Context.LAYOUT_INFLATER_SERVICE 로부터 LayoutInflater를 생성해온다.
        2) 바인딩 작업을 해야하는데, Item.xml을 바인딩으로 가져오되, inflate할 때에는 앞서 찾은 inflater 넣어주기, parent, false
        3) RecyclerView.ViewHolder를 상속받아 구현하는 부분에 대해서 itemView랑 현 item.xml의 binding.root를 매핑한다.
         */
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = WordItemBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    /*
    BindViewHolder에서는 데이터랑 뷰홀더랑 바인딩하는 작업을 한다. (데이터 호출 정보/화면에 보여주기 위한 뷰정보 포함)
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // list - Postion 이 원하는 데이터
        // item.xml에 있는 요소들과 list 포지션으로해서 각 요소들 바인딩 완료
        val word = list[position]
        holder.bind(word)
        // holder.itemView 접근 가능(각 요소 들)
        holder.itemView.setOnClickListener {
            itemClickListener?.onClick(word)
        }
        /*
        holder.binding.apply {
            textTextView.text = list[position].text
            meanTextView.text = list[position].mean
            typeChip.text = list[position].type
        }
        */
    }
}

interface ItemClickListener {
    fun onClick(word: Word)

}
