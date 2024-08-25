package com.example.part2.todaynotification

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.example.part2.todaynotification.databinding.ActivityMainBinding
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.ServerSocket
import java.net.Socket
import java.net.URI
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    val client = OkHttpClient()


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


        // OkHttpClient -> Http 통신이 막혀있음
        // https 통신 (Secure)
    }

    private fun initViews() {
        var serverHost: String = ""
        binding.serverHostEditText.addTextChangedListener {
            serverHost = it.toString()
        }
        binding.confirmButton.setOnClickListener {
            val request: Request = Request.Builder()
                .url("http://$serverHost:8080")
                .build()


            /*
            JSON 형식
            GSON (Json to Java/Kotlin)
             */

            // callback 구현체
            val callback = object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "수신에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                    }
                    Log.e("Callback Error", e.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    // 요청 결과가 잘 왔는데 서버로부터 실패 결과를 보낸 경우
                    // 응답 자체도 모두 성공적으로 돌아온 경우
                    if (response.isSuccessful) {
                        val responseBody = response.body?.string()

                        // responseBody를 JSON 형태로 받을것
                        // 이 Body값을 어떤 형태로 받을지는 앞서 셋팅한 Data class 형태로!!
                        val gson = Gson()
                        // val messsage = Message(responseBody ?: "")
                        // val responseData = gson.fromJson(messsage, Message::class.java)
                        val message = Gson().fromJson(responseBody, Message::class.java)

                        runOnUiThread {
                            binding.confirmButton.isVisible = false
                            binding.serverHostEditText.isVisible = false
                            binding.informationTextView.isVisible = true
                            binding.informationTextView.visibility = View.VISIBLE
                            binding.informationTextView.text = message.a
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this@MainActivity, "수신에 실패하였습니다.", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }

            }
            client.newCall(request).enqueue(callback)
        }
    }
}