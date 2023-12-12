package com.andry.data.repositories.ingredients

import com.andry.data.models.ingredients.IngredientsItem
import com.andry.utils.Resource
import kotlinx.coroutines.flow.Flow

interface IngredientsRepository {
    val allIngredients: Flow<List<IngredientsItem>>
    suspend fun getIngredientsFromDatabase(): Flow<Resource<List<IngredientsItem>>>
    suspend fun insertAllIngredientsToLocaleDb(ingredientsList: List<IngredientsItem>)

}