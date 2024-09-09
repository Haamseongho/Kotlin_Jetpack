package com.example.par2_chapter2

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.par2_chapter2.databinding.ActivityMainBinding
import java.io.IOError
import java.io.IOException

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null
    private var fileName: String = ""
    private var state: State = State.RELEASE

    // 앱 실행 = release 상태 -> 녹음중 -> 레코드 버튼 -> 저장(릴리즈 상태)
    // 릴리즈 -> 재생 -> 릴리즈
    private enum class State {
        RELEASE, RECORDING, PLAYING
    }

    companion object {
        const val TAG = "MAIN_ACTIVITY"
        const val RECORD_AUDIO_REQUEST_CODE = 101
    }

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

        initViews()
    }

    private fun initViews() {
        binding.playButton.setOnClickListener(this)
        binding.recordButton.setOnClickListener(this)
        binding.stopButton.setOnClickListener(this)
        fileName = "${externalCacheDir?.absolutePath}/audio_record_test.3gp"
    }

    private fun checkReadAudioPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED -> {
                // 실제로 녹음 시작하면 됨
                onRecord(true)
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this@MainActivity,
                Manifest.permission.RECORD_AUDIO
            ) -> {
                showPermissionRationalDialog()
            }
            // android에서의 requestPermissions는 첫 번째 인자가 context가 들어가지 않지만
            // androidX에서는 requestPermissions는 첫 번째 인자에 context가 들어갑니다.
            // androidX를 사용하기 위해서는 ActivirtyCompat에서 가져와야 합니다.
            else -> {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.RECORD_AUDIO),
                    RECORD_AUDIO_REQUEST_CODE
                )
            }
        }
    }

    private fun showPermissionRationalDialog() {
        AlertDialog.Builder(this).apply {
            setMessage("녹음 권한을 켜야 앱을 정상적으로 사용할 수 있습니다.")
            setPositiveButton("권한 허용하기") { _, _ ->
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.RECORD_AUDIO),
                    RECORD_AUDIO_REQUEST_CODE,
                )
            }
            setNegativeButton("취소") { dialogInterface, _ ->
                dialogInterface.cancel()
            }
        }.show()
    }

    // ActivityCompat.requestPermissions 의 응답값
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val audioRecordPermissionGranted =
            requestCode == RECORD_AUDIO_REQUEST_CODE && grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED
        if (audioRecordPermissionGranted) {
            // TODO 녹음 작업 시작
            onRecord(true)
        } else {
            // 교육용 팝업 필요
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@MainActivity,
                    Manifest.permission.RECORD_AUDIO
                )
            ) {
                showPermissionRationalDialog()
            }
            // 이미 교육용 팝업을 확인한 상태라면
            else {
                // 시스템 설정에서 직접 권한 주는 방향으로 진행할 것
                showPermissionSettingDialog()
            }
        }
    }


    private fun showPermissionSettingDialog() {
        // 설정에서 직접 수행
        AlertDialog.Builder(this).apply {
            setMessage("녹음 권한을 반드시 켜야만 앱을 정상적으로 사용할 수 있습니다.  설정에서 권한 키기")
            setPositiveButton("권한 허용하기") { _, _ ->
                navigateAppSettings()
            }
            setNegativeButton("취소") { dialogInterface, _ ->
                dialogInterface.cancel()
            }
        }.show()
    }

    private fun navigateAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts(
                "package",
                packageName,
                null
            )  // 상세 세팅으로 넘어가는데, 현재의 패키지의 상태로 앱 설정으로 넘어간다.
        }
        startActivity(intent)
    }

    // 위젯 클릭 이벤트
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.playButton -> {
                when (state) {
                    State.RELEASE -> {
                        onPlay(true)
                    } else -> {

                    }
                }

            }

            R.id.recordButton -> {
                when (state) {
                    State.RELEASE -> {
                        checkReadAudioPermission()
                    }

                    State.RECORDING -> {
                        onRecord(false)
                    }

                    State.PLAYING -> {

                    }
                }
            }

            R.id.stopButton -> {
                when(state) {
                    State.PLAYING -> {
                        onPlay(false)
                    } else -> {}
                }
            }
        }
    }

    private fun onPlay(start: Boolean) = if (start) startPlaying() else stopPlaying()

    private fun startPlaying() {
        state = State.PLAYING
        player = MediaPlayer().apply {
            try {
                setDataSource(fileName)
                prepare()
            } catch (e: IOException) {
                Log.e(TAG, "media player prepare failed $e")
            }
            start()
        }
        // play가 끝나는걸 확인하는 리스너
        player?.setOnCompletionListener {
            stopPlaying()
        }

        binding.recordButton.isEnabled = false
        binding.recordButton.alpha = 0.3f
    }

    private fun stopPlaying() {
        state = State.RELEASE
        player?.release()
        player = null
        binding.recordButton.isEnabled = true
        binding.recordButton.alpha = 1.0f
    }

    // 녹음 시작
    private fun onRecord(start: Boolean) = if (start) {
        startRecording()
    } else {
        stopRecording()
    }

    private fun startRecording() {
        state = State.RECORDING
        // 31 이상
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            recorder = MediaRecorder(this).apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setOutputFile(fileName)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                try {
                    prepare() // 준비시작
                } catch (e: IOException) {
                    Log.e(TAG, "prepare() failed $e")
                }

                start()
            }

            binding.recordButton.setImageResource(R.drawable.stop_button)
            binding.playButton.isEnabled = false
            binding.playButton.alpha = 0.3F
        }
        // 31미만 minSDK = 28
        else {
            recorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setOutputFile(fileName)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                try {
                    prepare() // 준비시작
                } catch (e: IOException) {
                    Log.e(TAG, "prepare() failed $e")
                }

                start()
            }
            binding.recordButton.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.stop_button
                )
            )
            binding.playButton.isEnabled = false
            binding.playButton.alpha = 0.3F
        }
    }

    private fun stopRecording() {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
        state = State.RELEASE

        binding.recordButton.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.record
            )
        )


        binding.playButton.isEnabled = true
        binding.playButton.alpha = 1.0f
    }
}