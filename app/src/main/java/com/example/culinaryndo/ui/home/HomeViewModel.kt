package com.example.culinaryndo.ui.home

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.culinaryndo.data.model.LoginResult
import com.example.culinaryndo.data.repository.CulinaryndoRepository

class HomeViewModel(private val repository: CulinaryndoRepository): ViewModel() {

    val isBookmarked = MutableLiveData<Boolean>()
    val bookmarkId = MutableLiveData<String>()
    private val _keyword = MutableLiveData<String>()
    val keyword: LiveData<String>
        get() = _keyword
    val location = MutableLiveData<Location>()



    fun getSession(): LiveData<LoginResult> {
        return repository.getSession().asLiveData()
    }

    fun getUserById(userId: String) = repository.getUserById(userId)

    fun getAllFoods() = repository.getAllFoods()

    fun getFoodById(id: Int) = repository.getFoodById(id)

    fun setKeyword(keyword: String){
        _keyword.value = keyword
        getFoodByKeyword(_keyword.value.toString())
    }
    fun getFoodByKeyword(keyword: String) =  repository.getFoodByKeywoord(keyword)

    fun getSpesificBookmark(userId: String, foodId: String) = repository.getSpesificBookmark(userId, foodId)

    fun addbookmark(userId: String, foodId: String) = repository.addBookmark(userId,foodId)

    fun deleteBookmark(userId: String, bookmarkId: String) = repository.deleteBookmark(userId,bookmarkId)

}