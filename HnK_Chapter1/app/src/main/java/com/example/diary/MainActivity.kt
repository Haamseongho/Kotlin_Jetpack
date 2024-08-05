package com.example.diary

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.diary.adapters.DiaryAdapter
import com.example.diary.databinding.ActivityMainBinding
import com.example.diary.models.REQUEST_IMAGE_PERMISSON

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var diaryAdapter: DiaryAdapter? = null
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

        initRecyclerView()
    }

    private fun initRecyclerView() {
        diaryAdapter = DiaryAdapter(object : DiaryAdapter.ItemClickListener {
            @RequiresApi(Build.VERSION_CODES.TIRAMISU)
            override fun onLoadMoreItems() {
                checkImagePermission()
            }
        })
        binding.mainRecyclerView
    }

    // 이미지 불러오기
    private fun loadImages() {

    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkImagePermission() {
        when {
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES)
                    == PackageManager.PERMISSION_GRANTED -> {
                loadImages()
            }

            shouldShowRequestPermissionRationale(android.Manifest.permission.READ_MEDIA_IMAGES) -> {
                showPermissionDialog()
            }

            else -> {
                requestPermissions()
            }
        }


    }

    // 질의할 것
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun showPermissionDialog() {
        AlertDialog.Builder(this).apply {
            title = "권한 제공이 필요합니다"
            setMessage("다음 앱을 사용하기 위해선 이미지 접근 권한이 반드시 필요합니다.")
            setPositiveButton("동의") { _, _ ->
                requestPermissions()
            }
            setNegativeButton("거부", null)
        }
    }

    // 권한 줄 것
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES),
            REQUEST_IMAGE_PERMISSON
        )
    }
}