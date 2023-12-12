package com.andry.data.models.ingredients

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity
data class IngredientsItem @JvmOverloads constructor(

    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    val id: Int,

    @SerializedName("foodId")
    val foodId: Int,

    @SerializedName("name")
    val name: String,

    @Transient
    var isSelected: Boolean = true


) : Parcelable