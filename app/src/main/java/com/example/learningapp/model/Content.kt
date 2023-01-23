package com.example.learningapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Content(

	@field:SerializedName("pages")
	val pages: List<PagesItem?>? = null,

	@field:SerializedName("id_content")
	val idContent: Int? = null
) : Parcelable