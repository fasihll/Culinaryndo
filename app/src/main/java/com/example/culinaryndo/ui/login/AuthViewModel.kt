package com.example.culinaryndo.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.culinaryndo.data.model.LoginResult
import com.example.culinaryndo.data.repository.CulinaryndoRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: CulinaryndoRepository): ViewModel() {
    fun getSession(): LiveData<LoginResult> {
        return repository.getSession().asLiveData()
    }

    fun saveSession(user: LoginResult){
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun login(email: String, password: String) = repository.login(email,password)

    fun register(fullname: String,username: String, email: String, password: String) = repository
        .register(fullname, username, email, password)
}