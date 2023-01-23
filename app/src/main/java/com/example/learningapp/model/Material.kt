package com.example.learningapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Material(

	@field:SerializedName("thumbnail_material")
	val thumbnailMaterial: String? = null,

	@field:SerializedName("title_material")
	val titleMaterial: String? = null,

	@field:SerializedName("id_material")
	val idMaterial: Int? = null
) : Parcelable