package com.example.culinaryndo.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.culinaryndo.data.model.LoginResult
import com.example.culinaryndo.data.repository.CulinaryndoRepository

class MainViewModel(private val repository: CulinaryndoRepository): ViewModel() {

    fun getSession(): LiveData<LoginResult> {
        return repository.getSession().asLiveData()
    }

    fun scanFood(foodName: String) = repository.scanFood(foodName)

    fun addHistory(userId: String, foodId: String) = repository.addHisotry(userId,foodId)

}