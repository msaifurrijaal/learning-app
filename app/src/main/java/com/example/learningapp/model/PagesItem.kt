package com.example.learningapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PagesItem(

	@field:SerializedName("parts_page")
	val partsPage: List<PartsPageItem?>? = null
) : Parcelable