package com.example.voca_book

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.voca_book.databinding.ActivityAddBinding
import com.example.voca_book.models.AppDatabase
import com.example.voca_book.models.Word
import com.google.android.material.chip.Chip
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBinding
    private var handler: Handler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initViews()
        handler = Handler(Looper.getMainLooper())
        binding.addButton.setOnClickListener {
            add()
        }
    }

    private fun add() {
        val text = binding.textInputEditText.text.toString()
        val mean = binding.meanTextInputEditText.text.toString()
        val chipId = binding.typeChipGroup.checkedChipId
        val type = findViewById<Chip>(chipId).text.toString() // 칩그룹안에 있는 아이디로 칩을 받아옴
        val word = Word(text, mean, type)
        CoroutineScope(Dispatchers.IO).launch {
            AppDatabase.getInstance(applicationContext)?.wordDao()?.insert(word)
            withContext(Dispatchers.Main) {
                Toast.makeText(applicationContext, "저장을 완료했습니다.", Toast.LENGTH_SHORT).show()
                val intent = Intent().putExtra("isUpdated", true) // 그냥 추가만 해도 넘어감
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    private fun initViews() {
        val types = listOf(
            "명사",
            "동사",
            "대명사",
            "형용사",
            "부사",
            "감탄사",
            "전치사",
            "접속사"
        )
        // types => collection
        binding.typeChipGroup.apply {
            types.forEach { type ->
                addView(createChip(type)) // chip 나온걸 addView로 넣어주기 (그룹에 넣기)
            }
        }
    }

    private fun createChip(text: String): Chip {
        return Chip(this).apply {
            setText(text)
            isCheckable = true
            isClickable = true
        }
    }
}