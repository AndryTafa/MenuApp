package com.andry.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Customer(

    @PrimaryKey(autoGenerate = false)
    @SerializedName("Email")
    val email: String,

    @SerializedName("Name")
    val name: String,

    @SerializedName("isLicenseActive")
    val isLicenseActive: Boolean,

    @SerializedName("isAdmin")
    val isAdmin: Boolean
): Parcelable