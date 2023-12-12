package com.andry.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Food @JvmOverloads constructor(

    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    val foodId: Int,

    @SerializedName("categoryId")
    val categoryId: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("price")
    val price: Double,

    @SerializedName("image")
    val image: String,

    @Transient
    var isAddIconVisible: Boolean = true
) : Parcelable