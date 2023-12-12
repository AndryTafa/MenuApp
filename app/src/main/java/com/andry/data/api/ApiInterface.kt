package com.andry.data.api

import com.andry.data.models.Category
import com.andry.data.models.Customer
import com.andry.data.models.Food
import com.andry.data.models.ingredients.IngredientsItem
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("api/customers")
    suspend fun getCustomers(): Response<List<Customer>>

    @GET("api/categories")
    suspend fun getCategories(): List<Category>

    @GET("api/foods")
    suspend fun getFood(): List<Food>

    @GET("api/ingredients")
    suspend fun getIngredients(): List<IngredientsItem>
}