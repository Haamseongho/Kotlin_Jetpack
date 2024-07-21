package com.example.chapter4

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.chapter4.databinding.InputActivityBinding

class InputActivity : AppCompatActivity() {
    companion object {
        const val TAG = "InputActivity"
    }

    private lateinit var binding: InputActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = InputActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, intent.getStringExtra("IntentMessage").toString())
    }
}