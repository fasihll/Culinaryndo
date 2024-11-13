package com.example.culinaryndo.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UpdateProfileResponse(

	@field:SerializedName("data")
	val data: UpdateProfileData? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
) : Parcelable

@Parcelize
data class UpdateProfileData(

	@field:SerializedName("imageUrl")
	val imageUrl: String? = null,

	@field:SerializedName("updated")
	val updated: List<Int?>? = null
) : Parcelable
