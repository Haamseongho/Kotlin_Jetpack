package com.example.diary.models

import android.net.Uri

sealed class DiaryContents {
    data class DiaryItems (
        val uri: Uri
    ) : DiaryContents()
    object LoadMore : DiaryContents()
}