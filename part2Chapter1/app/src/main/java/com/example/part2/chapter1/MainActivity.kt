package com.example.part2.chapter1

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.part2.chapter1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {

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

        initViews()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initViews() {
        val container = binding.fragmentContainer
        val button1 = binding.button1
        val button2 = binding.button2

        button1.setOnClickListener(this)
        button2.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.button1 -> {
                loadFragment(1)
            }
            R.id.button2 -> {
                loadFragment(2)
            }
        }
    }

    @SuppressLint("CommitTransaction")
    fun loadFragment(type: Int){
        // 액티비티 내부에 프레그먼트 붙일때 관리하는 녀석
        // beginTransaction -> 프레그먼트 작업 수행 시작(단위)

        when (type) {
            1 -> {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragmentContainer, WebViewFragment()) // 프래그먼트 컨테이너 replace 하는데, WebViewFragment() 가져오기
                    commit()
                }
            }
            2 -> {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragmentContainer, BFragment()) // 프래그먼트 컨테이너 replace 하는데, WebViewFragment() 가져오기
                    commit()
                }
            }
            else -> {

            }
        }
    }
}