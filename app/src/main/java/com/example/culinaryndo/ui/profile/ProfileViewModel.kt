package com.example.culinaryndo.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.culinaryndo.data.model.LoginResult
import com.example.culinaryndo.data.repository.CulinaryndoRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: CulinaryndoRepository): ViewModel() {
    fun logout(){
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getSession(): LiveData<LoginResult> {
        return repository.getSession().asLiveData()
    }

    fun getUserData(userId: String) = repository.getUserData(userId)

    fun getUserBookmark(userId: String) = repository.getUserBookmark(userId)
    fun getUserHisotry(userId: String) = repository.getUserHisotry(userId)
}