package com.example.diary

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.diary.adapters.DiaryAdapter
import com.example.diary.databinding.ActivityMainBinding
import com.example.diary.models.DiaryContents
import com.example.diary.models.REQUEST_IMAGE_PERMISSON

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var diaryAdapter: DiaryAdapter? = null
    private val imageLauncher =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uriList ->
            uploadImages(uriList)
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

        initRecyclerView()
    }
    private fun initRecyclerView() {
        diaryAdapter = DiaryAdapter(object : DiaryAdapter.ItemClickListener {
            @RequiresApi(Build.VERSION_CODES.TIRAMISU)
            override fun onLoadMoreItems() {
                checkImagePermission()
            }
        })
        binding.mainRecyclerView.apply {
            adapter = diaryAdapter
            layoutManager = GridLayoutManager(context, 2)
        }
    }

    /*
    1) registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uriList } -> 여러개 불러온 사진에 대한 uri 리스트
    2) uriList.map을 통해서 내부의 uri를 불러올 수 있음
    3) 모델이 되는 클래스 가지고와서 그 안에 uri 관리하는 data class에 접근해서 it(uri)를 넣어줌
    4) 어댑터의 현재 리스트는 List로 되어 있기 때문에 값 추가를 할 수 없기 때문에 toMutableList()로 값 넣을 수 있게 한다.
    5) apply 확장함수를 통해 MutableList의 addAll 함수를 통해 3번에서 처리한 이미지들을 다 넣어준다.
    6) 어댑터의 submitList를 가져와서 추가한 이미지를 넣어준다.  Images, updatedImages 모두 리스트이다.  고로 submitList에 들어갈 수 있다.
    7) submitList를 쓰게되면 ListAdapter쪽에서 데이터 관리를 알아서 해주고 추가되든 삭제되든 알아서 인덱스까지 관리해서 반환해준다.
     */
    private fun uploadImages(uriList: List<Uri>) {
        // uriList = 선택한 이미지의 uri를 리스트로 관리
        // uriList에 있는 image를 뽑아서 어댑터쪽에 이미지로 넣어주기
        val images = uriList.map {
            DiaryContents.DiaryItems(it) // 모델에서의 이미지의 Uri에 uriList에서 맵핑으로 가져오는 Uri를 넣어줌
        }
        // 내가 선택한 사진들 우선 아이템에 넣고 그 객체를 이제 리스트에 넣어줘야함
        val updatedImages = diaryAdapter?.currentList?.toMutableList()?.apply {
            addAll(images)
        }

        diaryAdapter?.submitList(updatedImages) // 이게 중요함 submitList에 넣어줘야 자동으로 데이터 관리하면서 리스트 셋팅함
    }


    // 이미지 불러오기
    private fun loadImages() {
        imageLauncher.launch("image/*")
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
        }.show()
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_IMAGE_PERMISSON -> {
                // grantResults에 결과가 들어가있는데 첫 번째꺼 접근
                val resultCode = grantResults.firstOrNull() ?: PackageManager.PERMISSION_DENIED
                if (resultCode == PackageManager.PERMISSION_GRANTED) {
                    loadImages()
                }
            }
        }
    }
}