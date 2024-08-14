package fastcampus.part0.chapter3_2

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.textView).setOnClickListener { (it as TextView).setTextColor((resources.getColor(R.color.purple_200, null)))  }
        findViewById<TextView>(R.id.textView).setOnLongClickListener {
            (it as TextView).text = "test"
            true
        }
    }
}