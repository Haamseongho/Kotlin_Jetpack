package com.example.chatapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.chatapp.chatList.ChatFragment
import com.example.chatapp.databinding.ActivityMainBinding
import com.example.chatapp.myPage.MyPageFragment
import com.example.chatapp.userList.UserFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val userFragment = UserFragment()
    private val chatFragment = ChatFragment()
    private val myPageFragment = MyPageFragment()
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

        val currentUser = Firebase.auth.currentUser

//        if(currentUser == null){
//            // 로그인안됨
//            startActivity(Intent(this, LoginActivity::class.java))
//            finish()
//        }

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.userList -> {
                    replaceFragment(userFragment) // 유저 Fragment만 뜨도록
                    return@setOnItemSelectedListener true  // setOnItemSelectedListener에서 보낸다를 고지하고 true로 주기
                }
                R.id.chatRoomList -> {
                    replaceFragment(chatFragment) // 유저 Fragment만 뜨도록
                    // replaceFragment() // chat Room Fragment
                    return@setOnItemSelectedListener true
                }
                R.id.myPage -> {
                    replaceFragment(myPageFragment)   // myPage Fragment
                    return@setOnItemSelectedListener true
                }
                else -> {
                    return@setOnItemSelectedListener false
                }
            }

        }

        replaceFragment(userFragment) // 초기엔 친구모록
    }

    private fun replaceFragment(fragment: Fragment){
        // fragment replace 하는 방법 익히기
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentLayout, fragment)
            commit()
        }
    }
}