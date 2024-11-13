package com.example.culinaryndo.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserResponse(
	val token: String? = null,
	val data: Data? = null,
	val message: String? = null,
	val status: Boolean? = null
) : Parcelable

@Parcelize
data class Data(
	val id: Int? = null,
	val fullname: String? = null,
	val email: String? = null,
	val username: String? = null,
	val urlImage: String? = null
) : Parcelable
