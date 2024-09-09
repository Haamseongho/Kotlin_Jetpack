package com.example.chatapp.userList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.DatabaseKey.Companion.DB_USERS
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentUserBinding
import com.example.chatapp.userList.adapter.UserAdapter
import com.example.chatapp.userList.model.UserItem
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
// Fragment의 생성자로 넣어주게 되면 onCreateView에서 셋팅해줄 필요 없음
class UserFragment : Fragment(
    //  R.layout.fragment_user
) {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentUserBinding
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUserBinding.bind(view) // view를 바인딩하기

        initViews()
    }

    private fun initViews() {
        val userListAdapter = UserAdapter()
        binding.userListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userListAdapter
        }
        // 데이터베이스 참조에서 DB_USERS 자식 컴포넌트로 접근
        // addValueEventListener
        // addListenerForSingleValueEvent
        Firebase.database.reference.child(DB_USERS)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {


                    /*
                    val list = snapshot.children.map {
                        it.getValue(UserItem::class.java)
                    }
                    이렇게하고 Adapter의 submitList에 넣어줘도 되긴 하지만, 이렇게 하게 되면 내 자신까지 포함되어
                    저장되게 됩니다.
                    따라서 forEach를 통해 유저를 뽑고 내 계정과 비교해서 아닌것만 친구목록에 넣는 방식으로 구현할 것입니다.
                     */

                    val userItemList = mutableListOf<UserItem>()
                    val currentUserId = Firebase.auth.currentUser?.uid ?: "" // 현재 로그인한 계정의 uid
                    snapshot.children.forEach {
                        val user =
                            it.getValue(UserItem::class.java) // UserItem이랑 가져온 데이터랑 매핑되어서 데이터 불러옴
                        user ?: return // user값이 없으면 리턴된다 -> 다음 iterator로 넘어감
                        if (user.userId != currentUserId) {
                            // 내 계정과 다른경우
                            userItemList.add(user)  // 나머지 친구목록(나 제외) 입력
                        }
                    } // children은 하나의 UserItem 객체 구조 하나로 가짐

                    userListAdapter.submitList(userItemList)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("UserFragment", error.message.toString())
                }
            })

        userListAdapter.submitList(
            mutableListOf<UserItem>().apply {
                add(UserItem("11", "22", "33"))
            }
        )
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}