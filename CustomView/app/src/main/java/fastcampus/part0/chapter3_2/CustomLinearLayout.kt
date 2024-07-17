package fastcampus.part0.chapter3_2

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout

class CustomLinearLayout @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    LinearLayout(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Log.i("그리기", "CustomLinearLayout - onMeasure")
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        Log.i("그리기", "CustomLinearLayout - onLayout")
        super.onLayout(changed, l, t, r, b)

    }

    override fun onDraw(canvas: Canvas?) {
        Log.i("그리기", "CustomLinearLayout - onDraw")
        super.onDraw(canvas)

    }
}