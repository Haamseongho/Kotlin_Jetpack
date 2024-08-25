package com.example.par2_chapter2

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

// Custom View 만들기
// Java 파일 입장에서는 뷰 여러개 만들어지는 것럼, 생성자 여러개를 지원해야함
// Kotlin으로 하면 이 생성자 하나만 만들어지므로 JvmOverloads 를 서서 여러 생성자 받을 수 있도록 하기
class WaveFormView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    // View 그림 그리기

    val rectF = RectF(20f, 30f, 20f + 30f, 30f + 60f)
    val redPaint = Paint().apply {
        color = Color.RED
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas?.drawRect(rectF, redPaint)
    }
}