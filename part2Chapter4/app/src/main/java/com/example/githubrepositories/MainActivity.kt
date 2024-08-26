package com.example.githubrepositories

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.githubrepositories.adapter.UserAdapter
import com.example.githubrepositories.databinding.ActivityMainBinding
import com.example.githubrepositories.model.Repo
import com.example.githubrepositories.service.Network
import com.example.githubrepositories.service.Service
import com.example.githubrepositories.service.proxy.ApiProxy
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.coroutineScope
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var binding: ActivityMainBinding
    private var network: Network? = null
    private var handler: Handler = Handler(Looper.getMainLooper())
    private var searchFor: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)


        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // proxy
        network = Network.getInstance()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            initViews()
        }

        // UserAdapter의 인자로 onClick을 두고 User를 인자로하여 보내는데
        /*
        UserAdapter 구현할 때 binding.root (부모뷰)에 setOnClickListener를 달아서
        onClick(User)를 구현하되, 반환값은 Unit으로 두어 Void (없다)로 셋팅합니다.
        따라서 UserAdapter 인자 구현할 때 User이고 여기에 Intent를 구현해서
        OnClick시, User를 Item으로 가지면서 화면 이동이 되도록 구현할 수 있다.
         */
        val userAdapter = UserAdapter {
            val intent = Intent(this@MainActivity, RepoActivity::class.java)
            intent.putExtra("username", it.username)
            startActivity(intent)
        }


        binding.userRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userAdapter
        }


        val runnable = Runnable {
            searchUsers(this@MainActivity, userAdapter, searchFor)
        }


        // debouncing?!
        // 비동기 - coroutine, rxJava
        binding.searchEditText.addTextChangedListener { text ->
            searchFor = text.toString()
            handler.removeCallbacks(runnable) // 새로운 값이 들어오면 기존꺼는 지울것
            handler.postDelayed(
                runnable,
                300
            )
            // 타이밍을 여러번 하지 않고 했을때 runnable 객체에 넘겨주고
            // 기존꺼가 있다면 지운 상태에서 다시 300밀리초의 텀으로 runnable을 검색한다.
        }

    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun initViews() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    fetchCountryCode(it)
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun fetchCountryCode(location: Location) {
        val geocoder = Geocoder(this, Locale.getDefault())
        geocoder.getFromLocation(
            location.latitude,
            location.longitude,
            1,
            android.location.Geocoder.GeocodeListener { addresses: MutableList<Address> ->
                if (addresses.isNotEmpty()) {
                    val address = addresses[0]
                    val countryCode = address.countryCode
                    Log.i("CountryCode :  ", countryCode)
                } else {
                    Log.d("CountryCode Error : ", "Error")
                }
            })
    }

    private fun searchUsers(context: Context, userAdapter: UserAdapter, query: String) {
        network?.getApiProxy()?.searchUsers(context, query, userAdapter)
    }
}