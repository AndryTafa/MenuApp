package com.andry.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity
data class Category(

    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    val categoryId: Int,

    @SerializedName("menuId")
    val menuId: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("image")
    val image: String
): Parcelable