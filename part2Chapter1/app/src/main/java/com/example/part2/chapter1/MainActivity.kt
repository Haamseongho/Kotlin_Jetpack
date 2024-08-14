package com.example.part2.chapter1

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.part2.chapter1.adapters.ViewPagerAdapter
import com.example.part2.chapter1.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity(), OnTabLayoutNameChanged {

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
        val sharedPreference = getSharedPreferences(WebViewFragment.Companion.SHARED_PREFERENCE, Context.MODE_PRIVATE)
        val tab0 = sharedPreference.getString("tab0_name","월요웹툰")
        val tab1 = sharedPreference.getString("tab1_name","화요웹툰")
        val tab2 = sharedPreference.getString("tab2_name","수요웹툰")
        binding.viewPager.adapter = ViewPagerAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager){ tab, position ->
            run {
//                val textView = TextView(this@MainActivity)
//                textView.text = "position $position"
//                textView.gravity = Gravity.CENTER
//                tab.customView = textView // customView -> 만든거 넣을 수 있음
                tab.text = when(position){
                    0 -> tab0
                    1 -> tab1
                    else -> tab2
                }
            }
        }.attach()
    }


    @Deprecated(
        "Deprecated in Java",
        ReplaceWith("super.onBackPressed()", "androidx.appcompat.app.AppCompatActivity")
    )
    override fun onBackPressed() {
        // supportFragmentManager에 fragment들을 리스트로 가지고 있음
        // viewPager의 순서를 fragments의 요소로 넣어서 supportFragmentManager로부터 찾음
        val currentFragment = supportFragmentManager.fragments[binding.viewPager.currentItem]// fragment를 ViewPager에서 가져와야함
        if (currentFragment is WebViewFragment) {
            if (currentFragment.canGoBack()) {
                currentFragment.goBack()
            } else {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }


    override fun nameChanged(position: Int, name: String) {
        val tab = binding.tabLayout.getTabAt(position) // 현재 탭의 위치를 통해 탭을 가져옴
        tab?.text = name
    }

}