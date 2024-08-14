package com.example.part2.chapter1

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.example.part2.chapter1.databinding.FragmentWebviewBinding

// Fragment ID로 찾아가는거
class WebViewFragment(private val position: Int, private val webViewUrl: String) : Fragment() {
    private lateinit var binding: FragmentWebviewBinding
    var listener: OnTabLayoutNameChanged ?= null  // WebViewFragment에 있는 리스너 -> 메인액티비티를 바라보도록 설정할 것
    companion object {
        const val SHARED_PREFERENCE = "WEB_HISTORY"
    }
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
        binding.webView.webViewClient = WebtoonWebViewClient(binding.progressBar) { url ->
            activity?.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE)?.edit {
                Log.d("WebViewFragment", "tab$position")
                putString("tab$position", url)
                // commit() // 동기처리 (바로 저장)
            }
        }
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.loadUrl(webViewUrl)


        // Button 클릭
        // 마지막 시점 기록해두기
        // Activity에 Fragment가 붙어있음 = 내가 어디 액티비티에 붙어있는지 알 수 있음
        binding.backToLastButton.setOnClickListener {
            val sharedPreference = activity?.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE)
            val url = sharedPreference?.getString("tab$position", "")
            if(url.isNullOrEmpty()){
                Toast.makeText(context, "마지막 저장 시점이 없습니다.", Toast.LENGTH_SHORT).show()
            } else {
                binding.webView.loadUrl(url)
            }
        }

        binding.changeTabNameButton.setOnClickListener {
            val dialog = AlertDialog.Builder(context)
            val editText = EditText(context)
            dialog.setView(editText)
            dialog.setPositiveButton("저장") { _, _ -> // dialogInterface, which 인데 안쓰니까
                // TODO 저장기능
                // Fragment도 액티비티에 붙어있는 녀석이기도 하고 확인버튼 눌렀을때 이름 변환 리스너에 대한 결과 처리는 Fragment가 붙어있는 액티비티에서 구현
                activity?.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE)?.edit {
                    putString("tab${position}_name", editText.text.toString())
                    listener?.nameChanged(position, "tab${position}_name") // 저장 누를때 현재 위치와 이름 보내기 (-> 메인액티비티로 보내기)
                }
            }
            dialog.setNegativeButton("취소") { dialogInterface, _ ->
                dialogInterface.cancel()
            }

            dialog.show()
        }
    }

    fun canGoBack(): Boolean {
        return binding.webView.canGoBack()
    }
    fun goBack() {
        binding.webView.goBack()
    }
}

interface OnTabLayoutNameChanged {
    fun nameChanged(position: Int, name: String)
}