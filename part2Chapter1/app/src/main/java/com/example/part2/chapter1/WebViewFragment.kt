package com.example.part2.chapter1

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.part2.chapter1.databinding.FragmentWebviewBinding

// Fragment ID로 찾아가는거
class WebViewFragment : Fragment() {
    private lateinit var binding: FragmentWebviewBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // View 반환 -> inflate 진행
        binding = FragmentWebviewBinding.inflate(inflater)
        return binding.root
    }

    // onCreateView 다음 작업
    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.webView.apply {
            webViewClient = WebtoonWebViewClient(binding.progressBar) // webViewClient를 커스텀해서 넣어주기
            settings.javaScriptEnabled = true
            loadUrl("https://comic.naver.com/")
        }

        // progressBar 로딩 멈추기 (다 끝났는지 체크)
    }
}