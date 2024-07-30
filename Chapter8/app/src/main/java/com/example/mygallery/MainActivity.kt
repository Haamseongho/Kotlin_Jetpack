package com.example.mygallery

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PackageManagerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mygallery.adapters.ImageAdapter
import com.example.mygallery.adapters.ImageItems
import com.example.mygallery.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val imageLoadLauncher =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uriList ->
            updateImages(uriList)
        }
    private var imageAdapter: ImageAdapter? = null

    companion object {
        const val REQUEST_READ_EXTERNAL_STORAGE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()

    }


    private fun initViews() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.loadImageButton.setOnClickListener {
            checkPermission()
        }
        initRecyclerView()
    }


    private fun initRecyclerView() {
        imageAdapter = ImageAdapter(object : ImageAdapter.ItemClickListener {
            override fun onLoadMoreClick() {
                checkPermission()
            }
        })

        binding.imageRecyclerView.apply {
            adapter = imageAdapter
            layoutManager = GridLayoutManager(context, 2)
        }
    }


    private fun checkPermission() {
        // 권한이 있는지 없는지 먼저 체크
        when {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                loadImage()
            }

            shouldShowRequestPermissionRationale(
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) -> {
                showPermissionInfoDialog()
            }

            else -> {
                requestReadExternalStorage()
            }
        }

    }

    private fun loadImage() {
        // Mime type 셋팅
        imageLoadLauncher.launch("image/*")
    }

    private fun showPermissionInfoDialog() {
        AlertDialog.Builder(this).apply {
            setMessage("이미지 가져오기를 위한 외부 저장소 권한 읽기 필요")
            setNegativeButton("취소", null)
            setPositiveButton("동의") { _, _ ->
                requestReadExternalStorage()
            }
        }.show()
    }

    private fun requestReadExternalStorage() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_READ_EXTERNAL_STORAGE
        ) // requestCode = 100
    }

    private fun updateImages(uriList: List<Uri>?) {
        Log.i("MainActivity", "$uriList")
        //

        val images = uriList?.map {
            ImageItems.Image(it)
        }
        val updatedImages = imageAdapter?.currentList?.toMutableList()?.apply {
            images?.let { addAll(it) }
        }  // 추가하는것 (변경된 리스트 자체를 넣고 싶음)
        // 기존에 있던거에서 추가된 것까지 정리되어서 보여줌
        imageAdapter?.submitList(updatedImages) // notify도 처리하고 Thread 관련 개념도 셋팅해줌 -> 계속 변경되는거 업데이트함
    }

    // Permission 요청결과에 대한 내용 확인
    // RequestPermissions에 대한 콜백을 관리
    // > requestReadExternalStorage() -> ActivityCompat.requestPermissions 처리
    // 해당 결과 콜백을 OnRequestPermissionsResult로 받음
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_READ_EXTERNAL_STORAGE -> {
                val resultCode = grantResults.firstOrNull() ?: PackageManager.PERMISSION_DENIED
                if (resultCode == PackageManager.PERMISSION_GRANTED) {
                    loadImage()
                }
            }
        }
    }
}