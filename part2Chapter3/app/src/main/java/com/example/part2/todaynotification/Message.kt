package com.example.part2.todaynotification

import com.google.gson.annotations.SerializedName

data class Message (
    @SerializedName("message") // a는 message로 관리할거야! (디컴파일해도 message로 안뜨고 a로 뜨기 때문에 안정성 확보)
    val a: String // Key -> 난독화 가능 (decompile 했을때 확인 불가)
)