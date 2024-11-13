package com.example.culinaryndo.ui.scan

import androidx.camera.core.ImageCapture
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.culinaryndo.data.model.ModelResult
import com.example.culinaryndo.data.repository.CulinaryndoRepository

class ScanViewModel(private val repository: CulinaryndoRepository): ViewModel() {
    val flashMode =  MutableLiveData<Int>()

    private val _modelResult = MutableLiveData<List<ModelResult>>()
    val modelResult: LiveData<List<ModelResult>> get() = _modelResult

    private val _mostFrequentHighScoreFoodItem = MutableLiveData<ModelResult?>()
    val mostFrequentHighScoreFoodItem: LiveData<ModelResult?> get() = _mostFrequentHighScoreFoodItem

   init {
       flashMode.value = ImageCapture.FLASH_MODE_OFF
   }
    fun addModelResult(modelResult: ModelResult) {
        val currentList = (_modelResult.value ?: emptyList()).toMutableList()
        currentList.add(modelResult)

        if (currentList.size > 150) {
            currentList.removeAt(0)
        }

        _modelResult.value = currentList

        updateMostFrequentHighScoreFoodItem()
    }

    private fun updateMostFrequentHighScoreFoodItem() {
        val mostFrequentHighScoreItem = _modelResult.value
            ?.groupingBy { it.name }
            ?.eachCount()
            ?.filter { it.value > 1 } // Select items that appear more than once
            ?.maxByOrNull { it.value } // Find the most frequent item
            ?.let { (name, _) ->
                _modelResult.value?.filter { it.name == name }?.maxByOrNull { it.score }
            }

        // Update LiveData
        _mostFrequentHighScoreFoodItem.value = mostFrequentHighScoreItem
    }
}