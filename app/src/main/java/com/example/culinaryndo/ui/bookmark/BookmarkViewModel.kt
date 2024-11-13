package com.example.culinaryndo.ui.bookmark

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.culinaryndo.data.model.LoginResult
import com.example.culinaryndo.data.repository.CulinaryndoRepository

class BookmarkViewModel(private val repository: CulinaryndoRepository): ViewModel() {
    fun getSession(): LiveData<LoginResult> {
        return repository.getSession().asLiveData()
    }

    fun getBookmarkByUser(userId: String) = repository.getUserBookmark(userId)

    fun deleteBookmark(userId: String, bookmarkId: String) = repository.deleteBookmark(userId,bookmarkId)

}