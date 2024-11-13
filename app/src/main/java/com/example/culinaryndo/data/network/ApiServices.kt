package com.example.culinaryndo.data.network

import com.example.culinaryndo.data.model.AddBookmarkResponse
import com.example.culinaryndo.data.model.AddHisotryResponse
import com.example.culinaryndo.data.model.BookmarkResponse
import com.example.culinaryndo.data.model.DefaultResponse
import com.example.culinaryndo.data.model.DetailResponse
import com.example.culinaryndo.data.model.FoodResponse
import com.example.culinaryndo.data.model.GooglePlaceApiResponse
import com.example.culinaryndo.data.model.HistoryResponse
import com.example.culinaryndo.data.model.UserResponse
import com.google.android.gms.maps.model.LatLng
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {
    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): UserResponse

    @FormUrlEncoded
    @POST("user/register")
    suspend fun register(
        @Field("fullname") fullname: String,
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): UserResponse

    @GET("user/get/{id}")
    suspend fun getUserById(
        @Path("id") userId: String
    ): UserResponse

    @GET("foods/get")
    suspend fun getAllFoods(): FoodResponse

    @GET("foods/get/{foodsId}")
    suspend fun getFoodById(
        @Path("foodsId") foodsId: Int
    ): DetailResponse

    @GET("bookmarks/{userId}")
    suspend fun getUserBookmark(
        @Path("userId") userId: String
    ): BookmarkResponse

    @GET("bookmarks/{userId}/{foodId}")
    suspend fun getSpesificBookmark(
        @Path("userId") userId: String,
        @Path("foodId") foodId: String,
    ): DetailResponse

    @FormUrlEncoded
    @POST("bookmarks/{userId}")
    suspend fun addBookmark(
        @Path("userId") username: String,
        @Field("foodsId") foodId: String,
    ) : AddBookmarkResponse

    @DELETE("bookmarks/{userId}/{bookmarkId}")
    suspend fun deleteBookmark(
        @Path("userId") userId: String,
        @Path("bookmarkId") bookmarkId: String,
    ) : DefaultResponse


    @GET("history/{userId}")
    suspend fun getUserHistory(
        @Path("userId") userId: String
    ): HistoryResponse

    @FormUrlEncoded
    @POST("history/{userId}")
    suspend fun addHitory(
        @Path("userId") username: String,
        @Field("foodsId") foodId: String,
    ) : AddHisotryResponse

    @DELETE("history/{userId}/{historyId}")
    suspend fun deleteHistory(
        @Path("userId") userId: String,
        @Path("historyId") bookmarkId: String,
    ) : DefaultResponse

    @GET("foods/searchFoodTitle/{keyword}")
    suspend fun getFoodByKeyword(
        @Path("keyword") keyword: String,
    ): FoodResponse

    @GET("foods/searchFood/{foodName}")
    suspend fun scanFood(
        @Path("foodName") foodName: String
    ): FoodResponse

    @Multipart
    @PUT("user/update/{userId}")
    suspend fun updateProfile(
        @Path("userId") userId: String,
        @Part file: MultipartBody.Part?,
        @Part("email") email: RequestBody,
        @Part("fullname") fullname: RequestBody,
        @Part("username") username: RequestBody,
        @Part("oldPassword") oldPassword: RequestBody?,
        @Part("newPassword") newPassword: RequestBody?,
    ): DefaultResponse

    @GET("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
    suspend fun getNarestPlaceFood(
        @Query("location") location: String,
        @Query("radius") radius: Int,
        @Query("type") type: String,
        @Query("key") key: String,
        @Query("keyword") keyword: String,
    ): GooglePlaceApiResponse
}