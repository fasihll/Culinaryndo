package com.example.culinaryndo.di

import android.content.Context
import com.example.culinaryndo.data.network.ApiConfig
import com.example.culinaryndo.data.pref.UserPreference
import com.example.culinaryndo.data.pref.dataStore
import com.example.culinaryndo.data.repository.CulinaryndoRepository

object Injection {
   fun provideCulinaryndoRepository(context: Context): CulinaryndoRepository {
       val pref = UserPreference.getInstance(context.dataStore)
       val apiService = ApiConfig.getApiServices()
       return CulinaryndoRepository(pref,apiService)
   }
}
