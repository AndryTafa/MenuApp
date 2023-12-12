package com.andry.data.repositories.food

import com.andry.data.models.Food
import com.andry.utils.Resource
import kotlinx.coroutines.flow.Flow

interface FoodRepository {
    val allFoods: Flow<List<Food>>
    suspend fun getFoodsFromDatabase(): Flow<Resource<List<Food>>>
    suspend fun insertAllFoodsToLocaleDb(foodList: List<Food>)

}