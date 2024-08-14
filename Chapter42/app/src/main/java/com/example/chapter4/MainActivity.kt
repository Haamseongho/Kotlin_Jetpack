package com.example.chapter4

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.example.chapter4.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding
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

        binding.floatingActionButton.setOnClickListener {
            intent = Intent(this, EditActivity::class.java).apply { putExtra("MOVE", "EditData") }
            startActivity(intent)
        }

        binding.deleteButton.setOnClickListener {
            initData()
        }

        binding.btnCall.setOnClickListener {
            with(Intent(Intent.ACTION_VIEW)) {
                val phoneNumber = binding.phoneBook.text.toString().replace("-","")
                data = Uri.parse("tel:$phoneNumber")
                startActivity(this)
            }
        }

        getDataUiUpdate()
    }

    override fun onResume() {
        getDataUiUpdate()
        super.onResume()
    }

    override fun onRestart() {
        getDataUiUpdate()
        super.onRestart()
    }

    private fun getDataUiUpdate() {
        with(getSharedPreferences(USER_INFORMATION, Context.MODE_PRIVATE)) {
            binding.birthDate.text = getString(BIRTHDATE, "미정")
            binding.name.text = getString(NAME, "미정")
            binding.bloodType.text = getString(BLOOD_TYPE, "미정")
            binding.phoneBook.text = getString(EMERGENCY_CONTACT, "미정")
            binding.etc.isVisible = getString(WARNING, "").isNullOrEmpty().not()
            binding.txtEtc.isVisible = getString(WARNING, "").isNullOrEmpty().not()
            if (!getString(WARNING, "").isNullOrEmpty()) {
                binding.etc.text = getString(WARNING, "미정")
            }
        }
    }//deleteButton

    private fun initData() {
        with(getSharedPreferences(USER_INFORMATION, Context.MODE_PRIVATE).edit()) {
            clear()
            apply()
            getDataUiUpdate()
        }
        Toast.makeText(this, "초기화를 완료했습니다.", Toast.LENGTH_SHORT).show()
    }
}