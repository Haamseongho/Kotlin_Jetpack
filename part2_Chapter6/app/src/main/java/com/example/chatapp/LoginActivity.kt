package com.example.chatapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chatapp.DatabaseKey.Companion.DB_USERS
import com.example.chatapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class   LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 회원가입
        binding.signUpButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            if(email.length < 8 || email.isEmpty()) {
                Toast.makeText(applicationContext, "이메일 형식을 지켜주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // 클릭 리스너로 돌아가기
            }

            if(password.isEmpty()){
                Toast.makeText(applicationContext, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
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
        // 로그인
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
                    val currentUser = Firebase.auth.currentUser
                    if(task.isSuccessful && currentUser != null){
                        val userId = currentUser.uid
                        val user = mutableMapOf<String, Any>()
                        user["userId"] = userId
                        user["username"] = email

                        // 미국으로 안하게되면 database("url")
                        // 예:) Firebase.database("https://~~~~~~~")
                        // child에 Key로 해서 가져오는데 이건 많이 쓰이기 때문에 상수값으로 넣어둠
                        // userId -> 내 아이디 (채팅방 관리할 때 쓰임) 알고 있기 때문에 child로 해서 다시 접근
                        Firebase.database.reference.child(DB_USERS).child(userId).updateChildren(user)
                        // 회원가입 성공
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        // 회원가입 실패
                        Toast.makeText(this, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                        Log.d("LoginActivity", task.exception.toString())
                    }
                }
        }
    }
}