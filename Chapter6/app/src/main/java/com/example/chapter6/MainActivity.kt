package com.example.chapter6

import android.annotation.SuppressLint
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import com.example.chapter6.databinding.ActivityMainBinding
import com.example.chapter6.databinding.DialogCountdownSettingBinding
import java.util.Timer
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var countdownSecond = 10
    private var currentDeciSecond = 0 // 1 -> 0.1, 10 -> 1초
    private var timerThread: Timer? = null
    private var currentCountDownDeciSecond = countdownSecond * 10 // 카운트다운 할 때 초
    val handler = Handler(Looper.getMainLooper())
    private lateinit var timeRunnable: Runnable
    var isRunning = false // 타이머 실행 상태

    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.countdownGroup.isVisible = true
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 카운트다운 클릭
        binding.countdownTextView.setOnClickListener {
            showCountdownSettingDialog()
        }

        binding.startButton.setOnClickListener {
            //start()
            if(!isRunning){
                isRunning = true
                start2()
            }

            binding.startButton.isVisible = false
            binding.stopButton.isVisible = false
            binding.pauseButton.isVisible = true
            binding.lapButton.isVisible = true
        }

        binding.stopButton.setOnClickListener {
            showAlertDialog()
        }

        binding.pauseButton.setOnClickListener {
            pause()
            binding.startButton.isVisible = true
            binding.stopButton.isVisible = true
            binding.pauseButton.isVisible = false
            binding.lapButton.isVisible = false
        }

        binding.lapButton.setOnClickListener {
            lap()
        }

        initCountDownViews()
    }

    private fun initCountDownViews() {
        binding.countdownTextView.text = String.format("%02d", countdownSecond)
        binding.countdownProgressBar.progress = 100
    }

    // Handler로 만들기
    private fun start2(){
        // 핸들러는 UI스레드에서 만들어줄 것
        // 메인 루퍼에서 핸들러를 계속 확인하는!

        // handler를 통해서 보낼 수 있는것 -> 메시지, Runnable 객체
        // object 형태의 Runnable로 접근
        timeRunnable = object: Runnable {
            override fun run() {
                if(!isRunning) return // 실행중이 아니라면 종료
                if (currentCountDownDeciSecond == 0) {
                    currentDeciSecond += 1
                    val minutes = currentDeciSecond.div(10) / 60
                    val seconds = currentDeciSecond.div(10) % 60
                    val deciSeconds = currentDeciSecond % 10
                    /*
                    1) runOnUiThread
                    2) View.post
                    3) View.postDelayed
                    4) handler -> handle message or handle runnable object
                     */
                    binding.timeTextView.text = String.format("%02d:%02d", minutes, seconds)
                    binding.tickTextView.text = deciSeconds.toString()
                    binding.countdownGroup.isVisible = false
                } else {
                    currentCountDownDeciSecond -= 1
                    val seconds = currentCountDownDeciSecond / 10
                    val progress =
                        (currentCountDownDeciSecond / (countdownSecond * 10f)) * 100
                    binding.countdownTextView.text = String.format("%02d", seconds)
                    binding.countdownProgressBar.progress = progress.toInt()
                }
                if (currentDeciSecond == 0 && currentCountDownDeciSecond < 31 && currentCountDownDeciSecond % 10 == 0) {
                    val toneType =
                        if (currentCountDownDeciSecond == 0) ToneGenerator.TONE_CDMA_HIGH_L else ToneGenerator.TONE_CDMA_ANSWER
                    ToneGenerator(AudioManager.STREAM_ALARM, ToneGenerator.MAX_VOLUME)
                        .startTone(toneType, 100) // duration 0.1초
                }

                handler.postDelayed(this, 100) // 100 ms
            }
        }
        handler.post(timeRunnable)
    }
    // 시간 체크 되는거 -> 워커스레드
    private fun start() {
        //  timer 만드는 것 = 스레드 만드는 것
        // 100ms = 0.1초

        timerThread = timer(initialDelay = 0, period = 100) {

            if (currentCountDownDeciSecond == 0) {
                currentDeciSecond += 1
                val minutes = currentDeciSecond.div(10) / 60
                val seconds = currentDeciSecond.div(10) % 60
                val deciSeconds = currentDeciSecond % 10
                /*
                1) runOnUiThread
                2) View.post
                3) View.postDelayed
                4) handler -> handle message or handle runnable object
                 */
                runOnUiThread {
                    binding.timeTextView.text = String.format("%02d:%02d", minutes, seconds)
                    binding.tickTextView.text = deciSeconds.toString()

                    binding.countdownGroup.isVisible = false
                }

            } else {
                currentCountDownDeciSecond -= 1
                val seconds = currentCountDownDeciSecond / 10
                val progress =
                    (currentCountDownDeciSecond / (countdownSecond * 10f)) * 100

                binding.root.post {
                    binding.countdownTextView.text = String.format("%02d", seconds)
                    binding.countdownProgressBar.progress = progress.toInt()
                }
            }

            if(currentDeciSecond == 0 && currentCountDownDeciSecond < 31 && currentCountDownDeciSecond % 10 == 0){
                val toneType = if(currentCountDownDeciSecond == 0) ToneGenerator.TONE_CDMA_HIGH_L else ToneGenerator.TONE_CDMA_ANSWER
                ToneGenerator(AudioManager.STREAM_ALARM, ToneGenerator.MAX_VOLUME)
                    .startTone(toneType, 100) // duration 0.1초
            }
        }
    }

    private fun stop() {
        binding.startButton.isVisible = true
        binding.stopButton.isVisible = true
        binding.pauseButton.isVisible = false
        binding.lapButton.isVisible = false

        currentDeciSecond = 0
        binding.timeTextView.text = "00:00"
        binding.tickTextView.text = "0"
        initCountDownViews()
        binding.countdownGroup.isVisible = true
        binding.lapContainerLinearLayout.removeAllViews() // 추가한 뷰들 모두 삭제

        isRunning = false
        handler.removeCallbacks(timeRunnable)
    }

    private fun pause() {
        timerThread?.cancel() // 취소 = 종료 = 일시정지
        timerThread = null // 타이머 초기화
        isRunning = false
        handler.removeCallbacks(timeRunnable)
    }


    @SuppressLint("SetTextI18n")
    private fun lap() {
        if(currentDeciSecond == 0){
            return // 시작 안한 상태
        }
        val container = binding.lapContainerLinearLayout
        TextView(this).apply {
            textSize = 20f // float로 넣어야함
            gravity = Gravity.CENTER
            setPadding(30) // 모든 곳에 적용
            // 1. 01:03 0 2. 02:08 3 이런식으로 쓰기
            // 컨테이너 안에 텍스트뷰 넣어야함
            val minutes = currentDeciSecond.div(10) / 60
            val seconds = currentDeciSecond.div(10) % 60
            val deciSeconds = currentDeciSecond % 10
            text = "${container.childCount.inc()}. " + String.format(
                "%02d:%02d %01d",
                minutes,
                seconds,
                deciSeconds
            )
        }.let { lapTextView ->
            container.addView(lapTextView, 0)  // 맨 위부터 계속 채워지게 Index는 0으로 두기
        }
    }

    private fun showCountdownSettingDialog() {
        AlertDialog.Builder(this).apply {
            val dialogBinding = DialogCountdownSettingBinding.inflate(layoutInflater)
            setView(dialogBinding.root)  // subView를 넣는거니까
            with(dialogBinding.countdownSecondPicker) {
                maxValue = 20
                minValue = 0
                value = countdownSecond
            }
            setTitle("카운트다운 설정")
            setPositiveButton("확인") { _, _ ->
                countdownSecond = dialogBinding.countdownSecondPicker.value
                currentCountDownDeciSecond = countdownSecond * 10
                binding.countdownTextView.text = String.format("%02d", countdownSecond)
            }
            setNegativeButton("취소", null)
        }.show()
    }

    private fun showAlertDialog() {
        // DatePickerDialog
        // TimePickerDialog
        // AlertDialog
        AlertDialog.Builder(this).apply {
            setMessage("종료하시겠습니까?")
            setPositiveButton("네") { _, _ ->
                stop()
            }
            setNegativeButton("아니오", null)
        }.show()
    }
}