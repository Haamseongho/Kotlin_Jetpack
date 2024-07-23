package com.example.chapter5

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chapter5.databinding.ActivityMainBinding
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MAINACTIVITY"
    }

    private lateinit var binding: ActivityMainBinding
    private val firstNumberText = StringBuilder("")
    private val secondNumberText = StringBuilder("")
    private val operatorText = StringBuilder("")
    private val decimalFormat = DecimalFormat("#,###")
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

    }

    fun numberClicked(v: View) {
        // 엘리스 연산자 사용
        // 버튼일 경우 텍스트 뽑고 버튼이 아닐 경우 공백
        val numberString = (v as? Button)?.text.toString() ?: ""
        val numberText = if(operatorText.isEmpty()) firstNumberText else secondNumberText
        numberText.append(numberString)
        updateEquationTextView()
    }

    fun clearClicked(v: View) {
        firstNumberText.clear()
        secondNumberText.clear()
        operatorText.clear()
        binding.resultTextView.text = ""
        updateEquationTextView()
    }

    @Suppress("IMPLICIT_CAST_TO_ANY")
    fun equalClicked(v: View) {
        if(firstNumberText.isNotEmpty() && secondNumberText.isNotEmpty() && operatorText.isNotEmpty()){
            val firstNumber = firstNumberText.toString().toBigDecimal()
            val secondNumber = secondNumberText.toString().toBigDecimal()
            val result = when(operatorText.toString()){
                "+" -> decimalFormat.format(firstNumber + secondNumber)
                "-" -> decimalFormat.format(firstNumber - secondNumber)
                else -> Toast.makeText(this, "잘못된 수식입니다.", Toast.LENGTH_SHORT).show()
            }.toString()

            binding.resultTextView.text = result
        }
        else {
            Toast.makeText(this, "수식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }
    }

    fun operatorClicked(v: View) {
        val operatorString = (v as? Button)?.text?.toString() ?: ""
        // operator X
        if(firstNumberText.isEmpty()){
            Toast.makeText(this, "먼저 숫자를 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }
        // secondNumberText가 있을땐 operator X
        if(secondNumberText.isNotEmpty()){
            Toast.makeText(this, "한 개의 연산자에 대해서만 연산이 가능합니다.", Toast.LENGTH_SHORT).show()
            return
        }
        operatorText.append(operatorString)
        updateEquationTextView()
    }

    private fun updateEquationTextView(){
        val firstFormattedNumber = if(firstNumberText.isNotEmpty()) decimalFormat.format(firstNumberText.toString().toBigDecimal()) else ""
        val secondFormattedNumber = if(secondNumberText.isNotEmpty()) decimalFormat.format(secondNumberText.toString().toBigDecimal()) else ""
        binding.equationTextView.text = "$firstFormattedNumber $operatorText $secondFormattedNumber"
    }
}