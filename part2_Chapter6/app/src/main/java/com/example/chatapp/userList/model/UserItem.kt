package com.example.chatapp.userList.model

// Firebase DB에 연동
data class UserItem(
    val userId: String ?= null,
    val username: String ?= null,
    val description: String ?= null,
)
// Firebase
// database.child("users").child(userId).setValue(user)
/*
영구 리스너로 데이터 읽기
addValueEventListener() -> UserItem에 데이터가 변경할 때마다 계속 발생

데이터 한 번만 읽기
get()
database.child("users").child(userId).get().addOnSuccessListener {

}
파이어베이스 -> addListenerForSingleValueEvent를 사용 -> 로컬 디스크 캐시에서 즉시 데이터 가져올 수 있음
-> 사용자 프로필 로드할 때 사용


* 데이터 목록 다루기
1) 추가
- push()
- ChildEventListener
 */