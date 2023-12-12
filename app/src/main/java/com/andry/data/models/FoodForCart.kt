package com.andry.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andry.data.models.ingredients.IngredientsItem
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class FoodForCart(

    @PrimaryKey(autoGenerate = false)
    @SerializedName("Food_ID")
    val foodId: Int,

    @SerializedName("Category_ID")
    val categoryId: Int,

    @SerializedName("Name")
    val name: String,

    @SerializedName("Price")
    val price: Double,

    @SerializedName("Image")
    val image: String,

    @SerializedName("Quantity")
    var quantity: Int,

    @SerializedName("Ingredients")
    var ingredients: List<IngredientsItem> = listOf(),




    ) : Parcelable