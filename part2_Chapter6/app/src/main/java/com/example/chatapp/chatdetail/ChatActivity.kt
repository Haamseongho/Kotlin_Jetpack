package com.example.chatapp.chatdetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.DatabaseKey
import com.example.chatapp.chatdetail.adapter.ChatAdapter
import com.example.chatapp.chatdetail.model.ChatItem
import com.example.chatapp.databinding.ActivityChatdetailBinding
import com.example.chatapp.userList.model.UserItem
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

// ChatRoomID를 넘겨받고 어디 방 들어갔는지 확인가능
// ChatRoomID를 통해 messageID, otherUserId 가져올 수 있음

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatdetailBinding
    private val chatAdapter = ChatAdapter()
    private var chatRoomId: String = ""
    private var otherUserId: String = ""
    private var myUserId: String = ""
    private var chatItemList = mutableListOf<ChatItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatdetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chatRoomId = intent.getStringExtra("chatRoomId") ?: ""
        otherUserId = intent.getStringExtra("otherUserId") ?: ""
        myUserId = Firebase.auth.currentUser?.uid ?: ""


        // DB 조회 2번
        Firebase.database.reference.child(DatabaseKey.DB_USERS).child(myUserId).get()
            .addOnSuccessListener {
                val myUserItem = it.getValue(UserItem::class.java)
                val myUserName = myUserItem?.username
            }

        // DB 조회 상대방 아이디
        Firebase.database.reference.child(DatabaseKey.DB_USERS).child(otherUserId).get()
            .addOnSuccessListener {
                val otherUserItem = it.getValue(UserItem::class.java)
                chatAdapter.otherUserItem = otherUserItem
            }

        // 채팅 가져오는 기능  chatRoomId에서 키를 가져올거임
        // ChildEventListener - callback : onChildAdded(), onChildChanged(), onChildRemoved(), onChildMoved()
        Firebase.database.reference.child(DatabaseKey.DB_CHATS).child(chatRoomId)
            .addChildEventListener(object: ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    // 메시지 추가
                    val chatItem = snapshot.getValue(ChatItem::class.java)
                    chatItem ?: return

                    chatItemList.add(chatItem) // 쓴거를 리스트에 계속 넣고 그 다음에 submitList에 담기
                    chatAdapter.submitList(chatItemList)
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    TODO("Not yet implemented")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        binding.chatRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chatAdapter
        }
    }
}