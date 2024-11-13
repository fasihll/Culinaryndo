package com.example.culinaryndo.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.culinaryndo.BuildConfig
import com.example.culinaryndo.data.Result
import com.example.culinaryndo.data.model.AddBookmarkResponse
import com.example.culinaryndo.data.model.AddHisotryResponse
import com.example.culinaryndo.data.model.BookmarkResponse
import com.example.culinaryndo.data.model.DefaultResponse
import com.example.culinaryndo.data.model.DetailResponse
import com.example.culinaryndo.data.model.FoodResponse
import com.example.culinaryndo.data.model.GooglePlaceApiResponse
import com.example.culinaryndo.data.model.HistoryResponse
import com.example.culinaryndo.data.model.Location
import com.example.culinaryndo.data.model.LoginResult
import com.example.culinaryndo.data.model.UserResponse
import com.example.culinaryndo.data.network.ApiServices
import com.example.culinaryndo.data.pref.UserPreference
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class CulinaryndoRepository(
    private val userPreference: UserPreference,
    private val apiService: ApiServices,
) {
    fun getSession(): Flow<LoginResult> {
        return userPreference.getSession()
    }

    suspend fun saveSession(user: LoginResult) {
        userPreference.saveSession(user)
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun register(
        fullname: String,
        username: String,
        email: String,
        password: String,
    ): LiveData<Result<UserResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.register(fullname, username, email, password)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, DefaultResponse::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        }
    }

    fun login(email: String, password: String): LiveData<Result<UserResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(email, password)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, DefaultResponse::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        }
    }

    fun getUserById(userId: String): LiveData<Result<UserResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getUserById(userId)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, DefaultResponse::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        }
    }

    fun getAllFoods(): LiveData<Result<FoodResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getAllFoods()
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, DefaultResponse::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        }
    }

    fun getFoodById(id: Int): LiveData<Result<DetailResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getFoodById(id)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, DefaultResponse::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        }
    }

    fun getSpesificBookmark(userId: String, foodId: String): LiveData<Result<DetailResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.getSpesificBookmark(userId, foodId)
                emit(Result.Success(response))
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, DefaultResponse::class.java)
                emit(Result.Error(errorResponse.message.toString()))
            }
        }

    fun addBookmark(userId: String, foodId: String): LiveData<Result<AddBookmarkResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.addBookmark(userId, foodId)
                emit(Result.Success(response))
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, DefaultResponse::class.java)
                emit(Result.Error(errorResponse.message.toString()))
            }
        }

    fun deleteBookmark(userId: String, bookmarkId: String): LiveData<Result<DefaultResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.deleteBookmark(userId, bookmarkId)
                emit(Result.Success(response))
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, DefaultResponse::class.java)
                emit(Result.Error(errorResponse.message.toString()))
            }
        }

    fun getFoodByKeywoord(keyword: String): LiveData<Result<FoodResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getFoodByKeyword(keyword)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, DefaultResponse::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        }
    }

    fun scanFood(foodName: String): LiveData<Result<FoodResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.scanFood(foodName)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, DefaultResponse::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        }
    }

    fun updateProfile(
        id: String,
        file: File?,
        fullname: String,
        username: String,
        email: String,
        oldPassword: String,
        newPassword: String,
    ): LiveData<Result<DefaultResponse>> = liveData {
        emit(Result.Loading)
        try {
            if(file == null){
                val email = email.toRequestBody("text/plain".toMediaType())
                val fullname = fullname.toRequestBody("text/plain".toMediaType())
                val username = username.toRequestBody("text/plain".toMediaType())
                val oldPassword = oldPassword.toRequestBody("text/plain".toMediaType())
                val newPassword = newPassword.toRequestBody("text/plain".toMediaType())

                val response = apiService.updateProfile(id, file = null, email, fullname, username,
                    oldPassword, newPassword)
                emit(Result.Success(response))
            } else{
                val fileRequestBody = file.asRequestBody("image/jpeg".toMediaType())
                val filePart = MultipartBody.Part.createFormData("file", file.name, fileRequestBody)

                val email = email.toRequestBody("text/plain".toMediaType())
                val fullname = fullname.toRequestBody("text/plain".toMediaType())
                val username = username.toRequestBody("text/plain".toMediaType())
                val oldPassword = oldPassword.toRequestBody("text/plain".toMediaType())
                val newPassword = newPassword.toRequestBody("text/plain".toMediaType())

                val response = apiService.updateProfile(id, filePart, email, fullname, username, oldPassword, newPassword)
                emit(Result.Success(response))
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, DefaultResponse::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        }
    }

    fun getUserData(userId: String): LiveData<Result<UserResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getUserById(userId)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, DefaultResponse::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        }
    }

    fun getUserBookmark(userId: String): LiveData<Result<BookmarkResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getUserBookmark(userId)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, DefaultResponse::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        }
    }

    fun getUserHisotry(userId: String): LiveData<Result<HistoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getUserHistory(userId)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, DefaultResponse::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        }
    }

    fun deleteHistory(userId: String, historyId: String): LiveData<Result<DefaultResponse>> =
        liveData {
        emit(Result.Loading)
        try {
            val response = apiService.deleteHistory(userId,historyId)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, DefaultResponse::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        }
    }

    fun addHisotry(userId: String, foodId: String): LiveData<Result<AddHisotryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.addHitory(userId,foodId)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, DefaultResponse::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        }
    }

    fun getNarestPlaceFood(location: LatLng,radiud: String,type: String,keyword: String):
            LiveData<Result<GooglePlaceApiResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getNarestPlaceFood("${location.latitude},${location.longitude}",
                radiud.toInt(),
                type,
                BuildConfig
                .MAPS_API_KEY,keyword)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, GooglePlaceApiResponse::class.java)
            emit(Result.Error(errorResponse.error_message.toString()))
        }
    }

}