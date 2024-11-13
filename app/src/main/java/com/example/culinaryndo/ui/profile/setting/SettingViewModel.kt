package com.example.culinaryndo.ui.profile.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.culinaryndo.data.model.LoginResult
import com.example.culinaryndo.data.repository.CulinaryndoRepository
import java.io.File

class SettingViewModel(private val repository: CulinaryndoRepository): ViewModel() {
    fun getSession(): LiveData<LoginResult> {
        return repository.getSession().asLiveData()
    }

    fun getUserData(userId: String) = repository.getUserData(userId)

    fun updateProfile(
        id: String, file: File?, fullname: String, username: String, email: String, oldPassword:
        String, newPassword: String) =
        repository.updateProfile(id,file,fullname,username,email,oldPassword,newPassword)
}