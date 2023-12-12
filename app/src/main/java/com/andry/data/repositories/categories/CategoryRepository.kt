package com.andry.data.repositories.categories

import com.andry.data.models.Category
import com.andry.utils.Resource
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    val allCategories: Flow<List<Category>>
    suspend fun getCategoriesFromDatabase(): Flow<Resource<List<Category>>>
    suspend fun insertCategoriesToLocaleDb(categoryList: List<Category>)
}