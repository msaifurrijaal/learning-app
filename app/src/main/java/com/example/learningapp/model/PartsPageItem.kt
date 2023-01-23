package com.example.learningapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PartsPageItem(

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("content")
	val content: String? = null
) : Parcelable