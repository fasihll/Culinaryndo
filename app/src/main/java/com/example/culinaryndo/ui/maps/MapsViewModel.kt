package com.example.culinaryndo.ui.maps

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.culinaryndo.data.repository.CulinaryndoRepository
import com.google.android.gms.maps.model.LatLng

class MapsViewModel(private val repository: CulinaryndoRepository): ViewModel() {
    val location = MutableLiveData<LatLng>()
    val foodname = MutableLiveData<String>()

    fun getNarestPlaceFood(location: LatLng,radiud: String,type: String,keyword: String) =
        repository.getNarestPlaceFood(location, radiud, type, keyword)
}