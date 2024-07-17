package com.example.countnumberapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
// import com.example.countnumberapp.data

// Kotlin 함수/생성자로 쓰임 -> 오버로딩 함수 생성 가능
// 기본값이 설정된 채로 구성할 수 있기 때문에 오버로딩 함수 여러개 만들 필요 없음
// 이러한 기능은 Kotlin에 대한 것이므로 자바에서도 쓸 수 있게 하는 어노테이션
// @JvmOverloads
@JvmOverloads
fun JvmTest(){
    println("Test")
}
class MainActivity : ComponentActivity() {
    companion object {
        const val TAG = "MainActivity"
    }
    //private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        JvmTest()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initViews()
    }

    private fun initViews() {
        var txtCount = findViewById<TextView>(R.id.txt_count)
        var number: Int = 0
        // btnPlus Clicked
        findViewById<AppCompatButton>(R.id.btn_plus).setOnClickListener { _ ->
            Log.d(TAG, "Plus button clicked")
            number += 1
            txtCount.text = txtCount.text.let { number.toString() }
        }
        // btnReset Clicked
        findViewById<AppCompatButton>(R.id.btn_reset).setOnClickListener { _ ->
            Log.d(TAG, "Reset button clicked")
            number = 0
            txtCount.text = txtCount.text.let { number.toString()}
        }
    }
}