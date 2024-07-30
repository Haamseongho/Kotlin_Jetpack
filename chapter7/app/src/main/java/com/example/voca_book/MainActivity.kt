package com.example.voca_book

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.voca_book.adapters.ItemClickListener
import com.example.voca_book.adapters.WordAdapter
import com.example.voca_book.databinding.ActivityMainBinding
import com.example.voca_book.models.AppDatabase
import com.example.voca_book.models.Word
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), ItemClickListener {
    private lateinit var wordAdapter: WordAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var list: MutableList<Word>
    private var selectWord:Word? = null



    // contract 넣고 callback 받음
    // Contracts에서 액티비티를 시작할건데 (startAcitivtyForResult) 이 결과값은
    // result로 받음

    // 받은걸 가지고 수정
    private val updateEditWordResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            // result내용으로 화면 UI 변경
            when(result.resultCode){
                RESULT_OK -> {
                    // Word -> Parcelable 데이터
                    val updatedWord = result.data?.getParcelableExtra<Word>(/* name = */ "editWord")// 받음
                    Log.d("haams_updateWord", "!!$updatedWord")
                    if(updatedWord != null){
                        updateEditWord(updatedWord)
                    }
                }
            }
        }

    private val updateAddWordResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            Log.d("haams_isUpdated", "isUpdated : ??")
            if(result.resultCode == RESULT_OK){
                val isUpdated = result.data?.getBooleanExtra("isUpdated", false) ?: false
                Log.d("haams_isUpdated", "isUpdated : $isUpdated")
                if(isUpdated){
                    Log.d("haams_isUpdated2", "updateADdWord?!@!")
                    updateAddWord()
                }
            }
        }

    private fun updateAddWord() {
        CoroutineScope(Dispatchers.IO).launch {
            val latestWord = AppDatabase.getInstance(applicationContext)?.wordDao()?.getLatestWord()
            withContext(Dispatchers.Main){
                latestWord?.let {
                    word ->
                    wordAdapter.list.add(0, word[0])
                    wordAdapter.notifyDataSetChanged()  // UI가 변경될 동작의 트리거가 되므로 DisPatchers.Main에서
                }
            }
        }
    }

    private fun updateEditWord(word: Word){
        val index = wordAdapter.list.indexOfFirst { it.id == word.id }
        wordAdapter.list[index] = word // 해당 인덱스에 word로 변경
        CoroutineScope(Dispatchers.IO).launch {
            selectWord = word
            withContext(Dispatchers.Main){
                wordAdapter.notifyItemChanged(index)
                binding.textTextView.text = word.text
                binding.meanTextView.text = word.mean
            }
        }
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
        // 단어 추가
        binding.addButton.setOnClickListener {
             // -> AddActivity로 값을 보내고 그 결과를 계속 리스닝함
            // AddActivity에서 setResult를 통해 값을 받을 수 있음
            // setResult에 셋팅한 값을 가져올 수 있음(듣기 가능)
            // launch를 써서 값 가져오기 (setResult)

            // startActivity -> startActivityForResult(> registerActivityForResult를 통해 보냄)
            // launch를 하게되면 it 부분은 intent니까 자동으로 보내지고 setResult로 값 넘기고
            // 넘길때 intent.putExtra로 보낸 값을 받을 수 있음
            Intent(this, AddActivity::class.java).let {
                updateAddWordResult.launch(it)
            }
        }
        // 단어 삭제
        binding.deleteImageView.setOnClickListener {
            delete()
        }

        // 수정
        binding.editImageView.setOnClickListener {
            edit()
        }
    }




    private fun initRecyclerView() {

        wordAdapter = WordAdapter(mutableListOf(), this) // clickListener를 여기서 구현함 (interface)
        // binding의 apply 속성 구성이기에 applicationContext로
        binding.wordRecyclerView.apply {
            adapter = wordAdapter
            layoutManager = LinearLayoutManager(
                applicationContext,
                LinearLayoutManager.VERTICAL,
                false
            ) // reverseLayout = X 화면 변경 X
            val dividerItemDecoration =
                DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL)
            addItemDecoration(dividerItemDecoration)
        } // adapter

        CoroutineScope(Dispatchers.IO).launch {
            list = (AppDatabase.getInstance(applicationContext)?.wordDao()?.getAll()
                ?: emptyList()).toMutableList()
            withContext(Dispatchers.Main) {
                wordAdapter.list.addAll(list) // 데이터 넣었으니 화면 로드할 것
                wordAdapter.notifyDataSetChanged()
            }
        }
    }
    private fun delete() {
        if(selectWord == null){
            return
        }
        CoroutineScope(Dispatchers.IO).launch {
            selectWord?.let {word ->
                AppDatabase.getInstance(applicationContext)?.wordDao()?.delete(word)
                wordAdapter.list.remove(word)
            }
            withContext(Dispatchers.Main) {
                wordAdapter.notifyDataSetChanged()
                binding.textTextView.text = ""
                binding.meanTextView.text = ""

                Toast.makeText(applicationContext, "삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun edit(){
        if(selectWord == null) return
        val eIntent = Intent(this, AddActivity::class.java).putExtra("originalData", selectWord)
        updateEditWordResult.launch(eIntent)
    }
//
//    override fun onResume() {
//        loadDataAll()
//        super.onResume()
//    }

    override fun onClick(word: Word) {
        selectWord = word // 선택한게 해당 값
        binding.textTextView.text = selectWord?.text
        binding.meanTextView.text = selectWord?.mean

    }

}