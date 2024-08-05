package com.example.mygallery

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore.Images
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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
import com.example.mygallery.frames.FrameActivity

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
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loadImageButton.setOnClickListener {
            checkPermission()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.toolbar.apply {
            title = "사진가져오기"
            setSupportActionBar(this)
        }
        initRecyclerView()
        binding.navigateFrameActivityButton.setOnClickListener {
            navigateToFrameActivity()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                checkPermission()
                true
            }

            else -> {
                 super.onOptionsItemSelected(item)
            }
        }
    }

    private fun navigateToFrameActivity() {
        val images = imageAdapter?.currentList?.filterIsInstance<ImageItems.Image>()
            ?.map { it.uri.toString() }?.toTypedArray()
        val intent = Intent(this, FrameActivity::class.java)
            .putExtra("images", images)

        startActivity(intent)
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
                android.Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED -> {
                loadImage()
            }

            shouldShowRequestPermissionRationale(
                android.Manifest.permission.READ_MEDIA_IMAGES
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES),
                REQUEST_READ_EXTERNAL_STORAGE
            )
        } // requestCode = 100
    }

    /*
    registerForActivityResult를 통해서 갤러리에 이미지를 선택하는 절차를 넣어둿는데 여기서는 uriList를 콜백으로 받습니다.
    이것은 갤러리로부터 이미지를 선택했을때 각 이미지의 uri를 List로 만든 것입니다.
    문제는 이 선택한 이미지를 어댑터의 모델이 되고 있는 ImageItems의 Image에 넣어주는 것이 중요합니다.
    그리고 이것은 지금 선택한 이미지의 uri를 단순히 모델쪽인 ImageItems의 image에 넣어준 상태이며
    현재 ImageItems(새로 선택한 값들이 들어간 이미지)에 기존 이미지를 넣어주는 작업을 해야합니다.
     */
    private fun updateImages(uriList: List<Uri>?) {
        Log.i("MainActivity", "$uriList")
        // uriList?.map 을 통해 현재 리스트 안의 Uri를 반환해낼 수 있고 이걸 ImageItems.Image에 넣어주는 것을 합니다.
        val images = uriList?.map {
            ImageItems.Image(it)
        }
        // 기존 ImageAdapter에서 사용하는 현재 리스트의 mutableList를 가져오면
        // 이는 ImageItems의 리스트일것이고 앞서 뽑은 Images에 .let 확장함수를 써서
        // addAll(it -> 기존에 있던 이미지)를 넣어 진행합니다.
        // imageAdapter.submitList(updatedImages) 하면 데이터 Notify를 수행합니다.
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