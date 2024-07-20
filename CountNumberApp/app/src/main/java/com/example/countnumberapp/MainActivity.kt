package com.example.countnumberapp

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
// import com.example.countnumberapp.data

// Kotlin 함수/생성자로 쓰임 -> 오버로딩 함수 생성 가능
// 기본값이 설정된 채로 구성할 수 있기 때문에 오버로딩 함수 여러개 만들 필요 없음
// 이러한 기능은 Kotlin에 대한 것이므로 자바에서도 쓸 수 있게 하는 어노테이션
// @JvmOverloads

class MainActivity : ComponentActivity() {

    private var number: Int = 0
    companion object {
        const val TAG = "MainActivity"
        const val STATE_CODE = "1001"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("onCreate()!!!!!!!!")
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val txtCount = findViewById<TextView>(R.id.txt_count)
        val btnPlus = findViewById<AppCompatButton>(R.id.btn_plus)
        val btnReset = findViewById<AppCompatButton>(R.id.btn_reset)

        if(savedInstanceState != null){
            txtCount.text = savedInstanceState.getInt(STATE_CODE).toString()
        }

        btnPlus.setOnClickListener{ _ ->
            number += 1
            txtCount.text = number.toString()
        }

        btnReset.setOnClickListener{ _ ->
            number = 0
            txtCount.text = number.toString()
        }

    }

    override fun onStart() {
        println("onStart()!!!!!")
        super.onStart()
    }
    override fun onResume() {
        println("onResume()!!!!!")
        super.onResume()
    }
    // OnStart() 메소드 다음에 호출되는 녀석
    // 시스템은 복원할 저장 상태가 있을 경우에만 호출(Bundle에 값을 저장해 놓을 경우에만 이게 호출된다는 것)
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        println("onRestoreInstanceState!!!!!!!")
        number = savedInstanceState.getInt(STATE_CODE)
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        println("haams_number : ${number}")
        outState?.run {
            putInt(STATE_CODE, number)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onPause() {
        println("Pause!!!!!")
        super.onPause()
    }

    override fun onStop() {
        println("stop!!!!")
        super.onStop()
    }

    override fun onDestroy() {
        println("onDestroy!!!!")
        super.onDestroy()
    }
}

// 구성변경 -> 세로/가로 모드 변경
// onPause, onStop, onDestroy 콜백 트리거 후 onCreate, onStart 및 onResume이 트리거됨
// 임시 UI 상태 저장 및 복원
/*
회전 또는 멀티 윈도우 모드로의 전환이어도 값은 유지되길 원함
시스템 구성 변경 발생시 활동을 소멸시켜 UI 상태가 제거
시스템 제약으로 인해 활동이 소멸되면 ViewModel, onSavedInstanceState() 및 로컬 저장소 결합을 통해 임시 UI 상태를 보존
onStop이 호출이 되고나서 onSavedInstanceState가 호출됩니다.
즉, onDestroy가 되어서 정보가 다 날라가기전에 onSavedInstanceState에 데이터를 저장할 기회를 줌
Bundle 객체로 값 저장가능

활동이 이전에 소멸된 후 재생성되면, 시스템이 활동에 전달하는 Bundle로 부터 저장된 인스턴스 상태를 복구할 수 있음
-> onCreate() 및 onRestoreInstanceState() 콜백 메서드 둘 다 인스턴스 상태 정보를 포함하는 동일한 Bundle을 수신
 */