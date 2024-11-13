package com.example.culinaryndo.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.culinaryndo.data.model.LoginResult
import com.example.culinaryndo.data.repository.CulinaryndoRepository

class HistoryViewModel(private val repository: CulinaryndoRepository): ViewModel() {
    fun getSession(): LiveData<LoginResult> {
        return repository.getSession().asLiveData()
    }

    fun getHistoryByUser(userId: String) = repository.getUserHisotry(userId)

    fun deleteHistory(userId: String, hisotryId: String) = repository.deleteHistory(userId, hisotryId)
}