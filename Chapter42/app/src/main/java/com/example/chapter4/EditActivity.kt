package com.example.chapter4

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.chapter4.databinding.EditActivityBinding

class EditActivity : AppCompatActivity() {
    companion object {
        const val TAG = "InputActivity"
    }

    private lateinit var binding: EditActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, intent.getStringExtra("IntentMessage").toString())

        binding.bloodTypeSpinner.adapter = ArrayAdapter.createFromResource(
            this,
            R.array.blood_type,
            android.R.layout.simple_list_item_1
        )
        // DatePicker 추가
        binding.birthDateLayer.setOnClickListener {
            var listener = OnDateSetListener { date, year, month, dateOfMonth ->
                binding.birthdateValueTextView.text = "$year.${month.inc()}.$dateOfMonth"
            }
            DatePickerDialog(this, listener, 2000, 1, 1).show()
        }

        binding.warningCheckBox.setOnCheckedChangeListener { _, isChecked ->
            binding.warningEditText.isVisible = isChecked
        }

        binding.warningEditText.isVisible = binding.warningCheckBox.isChecked

        binding.saveButton.setOnClickListener {
            saveData()
            finish()
        }
    }

    private fun saveData() {
        // sharedpreference 사용 > 보통 세션을 관리할 때 쓰이며 키-값 데이터로 저장
        // Key - Value & Mode 설정
        // Context. MODE_PRIVATE 와 같은 종류가 많음
        with(getSharedPreferences(USER_INFORMATION, Context.MODE_PRIVATE).edit()) {
            putString(NAME, binding.nameEditText.text.toString())
                .putString(BLOOD_TYPE, getBloodType())
                .putString(EMERGENCY_CONTACT, binding.emergencyContactEditText.text.toString())
                .putString(BIRTHDATE, binding.birthdateTextView.text.toString())
                .putString(WARNING, getWarning())
            apply()
        }

        Toast.makeText(this, "저장을 완료했습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun getBloodType(): String {
        val bloodAlphabet = binding.bloodTypeSpinner.selectedItem.toString()
        val bloodSign = if (binding.bloodTypePlus.isChecked) "+" else "-"
        val result = bloodAlphabet.plus(bloodSign)
        return result
    }

    private fun getWarning(): String {
        return if (binding.warningCheckBox.isChecked) {
            binding.warningEditText.text.toString()
        } else {
            ""
        }
    }
}