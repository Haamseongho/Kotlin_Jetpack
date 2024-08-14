package com.example.part2.chapter1

import android.graphics.Bitmap
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar


// Unit -> 무엇이 들어와도 됨 (함수) : 리턴형 Void (Unit)
class WebtoonWebViewClient(
    private val progressBar: ProgressBar,
    private val saveData: (String) -> Unit
): WebViewClient() {

    // 이 때 progressBar 끄면 될 듯?
    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        // progressBar를 가져오거나 callback으로 처리하거나
        progressBar.visibility = GONE
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        progressBar.visibility = VISIBLE
    }

    // shouldOverrideUrlLoading
    // request를 보고 이걸 로드할지 말지 결정하는 함수
    // true => page load X
    // false => page load 계속 시도
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        Log.d("WebtoonWebViewClient", request?.url.toString())
        saveData.invoke(request?.url.toString()) // request의 Url 보내서 저장시킬것
        return !(request != null && request.url.toString().contains("google"))
    }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)
    }
}