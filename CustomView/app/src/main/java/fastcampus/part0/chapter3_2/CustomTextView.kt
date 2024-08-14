package fastcampus.part0.chapter3_2

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView

class CustomTextView  @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    AppCompatTextView(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Log.i("그리기", "CustomTextView - onMeasure")
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        Log.i("그리기", "CustomTextView - onLayout")
        super.onLayout(changed, l, t, r, b)

    }

    override fun onDraw(canvas: Canvas?) {
        Log.i("그리기", "CustomTextView - onDraw")
        super.onDraw(canvas)
    }

    override fun requestLayout() {
        Log.i("그리기", "CustomTextView - requestLayout")
        super.requestLayout()
    }

    override fun invalidate() {
        Log.i("그리기", "CustomTextView - invalidate")
        super.invalidate()
    }
}