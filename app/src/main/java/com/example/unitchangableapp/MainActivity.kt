package com.example.unitchangableapp

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.viewbinding.ViewBinding
import com.example.unitchangableapp.databinding.ActivityMainBinding
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var cmToM = true
    private var inputNumber : Int = 0
    companion object {
        const val CMTOM_CODE = "1001"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if(savedInstanceState != null){
            cmToM = savedInstanceState.getBoolean(CMTOM_CODE)
        }

        val inputEditText = binding.edtUnit
        val inputUnitText = binding.txtCmM
        var outputTextView = binding.txtUnit
        val outputUnitText = binding.txtMCm
        val convertButton = binding.btnConvert


        inputEditText.addTextChangedListener { text ->
            inputNumber =
                if (text.isNullOrEmpty()) {
                    0
                } else {
                    text.toString().toInt()
                }

            if(cmToM){
                outputTextView.text = inputNumber.times(0.01).toString()
            } else {
                outputTextView.text = inputNumber.times(100).toString()
            }
        }
        // 처음엔 Cm니까 거기서 outputTextView가 inputTextView의 값에 0.01을 곱하는 것
        convertButton.setOnClickListener {
            cmToM = !cmToM
            if(cmToM){
                outputTextView.text = inputNumber.times(0.01).toString()
                inputUnitText.text = "cm"
                outputUnitText.text = "m"
            }
            // m -> cm
            else {
                outputTextView.text = inputNumber.times(100).toString()
                inputUnitText.text = "m"
                outputUnitText.text = "cm"
            }
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        cmToM = savedInstanceState.getBoolean(CMTOM_CODE)
        super.onRestoreInstanceState(savedInstanceState)
    }

    // onDestroy 전에 값 저장
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(CMTOM_CODE, cmToM)
        super.onSaveInstanceState(outState)
    }
}