package com.example.chatapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chatapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 로그인
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            if(email.length < 8 || email.isEmpty()) {
                Toast.makeText(applicationContext, "이메일 형식을 지켜주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // 클릭 리스너로 돌아가기
            }

            if(password.isEmpty()){
                Toast.makeText(applicationContext, "비밀번호 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // 클릭 리스너로 다시 돌아가기
            }
            Firebase.auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this){task ->
                    if(task.isSuccessful){
                        // 로그인 성공
                        Toast.makeText(this, "회원가입 성공!  로그인을 해주세요.", Toast.LENGTH_SHORT).show()
                    } else {
                        // 회원가입 실패
                        Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        // 회원가입
        binding.signInButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            if(email.length < 8 || email.isEmpty()) {
                Toast.makeText(applicationContext, "이메일 형식을 지켜주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // 클릭 리스너로 돌아가기
            }

            if(password.isEmpty()){
                Toast.makeText(applicationContext, "비밀번호 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // 클릭 리스너로 다시 돌아가기
            }

            Firebase.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this){task ->
                    if(task.isSuccessful){
                        // 로그인 성공
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        // 로그인 실패
                        Toast.makeText(this, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                        Log.d("LoginActivity", task.exception.toString())
                    }
                }
        }
    }
}