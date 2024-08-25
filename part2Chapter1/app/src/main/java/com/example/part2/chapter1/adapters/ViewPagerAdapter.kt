package com.example.part2.chapter1.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.part2.chapter1.MainActivity
import com.example.part2.chapter1.OnTabLayoutNameChanged
import com.example.part2.chapter1.WebViewFragment

// 인자 : FragmentManager, LifeCycle
/*
FragmentActivity를 보낼수도 있고 Fragment 안에 Fragment를 만들어서 보낼 수도 있음
 */
class ViewPagerAdapter(private val mActivity: MainActivity) :
    FragmentStateAdapter(mActivity) {

    override fun getItemCount(): Int {
        return 3
    }

    // 이 때 리스너를 달아줄 것
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                return WebViewFragment(position, "https://www.google.com").apply {
                    listener = mActivity
                }
            }
            1 -> {
                return WebViewFragment(position, "https://search.naver.com/search.naver?sm=tab_hty.top&where=nexearch&ssc=tab.nx.all&query=%EA%B7%B8%EB%A6%B0%EB%A6%AC%EC%86%8C%EC%8A%A4&oquery=%EA%B7%B8%EB%A6%B0%EB%A6%AC%EC%86%8C%EC%8A%A4&tqi=irw3Awqo1Sossf%2F09f0ssssssoZ-362163").apply {
                   listener = mActivity
                }
            }
            else -> {
                return WebViewFragment(position, "https://haams704.tistory.com/").apply {
                    listener = mActivity
                }
            }
        }
    }

}