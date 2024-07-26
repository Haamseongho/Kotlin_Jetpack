package com.example.voca_book

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.voca_book.adapters.ItemClickListener
import com.example.voca_book.adapters.WordAdapter
import com.example.voca_book.databinding.ActivityMainBinding
import com.example.voca_book.models.Word

class MainActivity : AppCompatActivity(), ItemClickListener {
    private lateinit var wordAdapter: WordAdapter
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initRecyclerView()

        binding.addButton.setOnClickListener {
            startActivity(Intent(this, AddActivity::class.java))
        }
    }



    private fun initRecyclerView(){

        wordAdapter = WordAdapter(mutableListOf(), this) // clickListener를 여기서 구현함 (interface)
        // binding의 apply 속성 구성이기에 applicationContext로
        binding.wordRecyclerView.apply {
            adapter = wordAdapter
            layoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL, false) // reverseLayout = X 화면 변경 X
            val dividerItemDecoration = DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL)
            addItemDecoration(dividerItemDecoration)
        } // adapter
    }
    override fun onClick(word: Word) {
        Toast.makeText(this, "${word.text} ${word.mean} ${word.type} 클릭완료", Toast.LENGTH_SHORT).show()
    }

}