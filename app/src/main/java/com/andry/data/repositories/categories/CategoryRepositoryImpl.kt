package com.andry.data.repositories.categories

import com.andry.data.api.ApiService
import com.andry.data.models.Category
import com.andry.data.room.daos.CategoriesDao
import com.andry.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val categoriesDao: CategoriesDao,
    private val apiService: ApiService,
) :
    CategoryRepository {

    override val allCategories: Flow<List<Category>>
        get() = categoriesDao.getAll().flowOn(Dispatchers.IO)

    override suspend fun getCategoriesFromDatabase(): Flow<Resource<List<Category>>> = flow {
        emit(Resource.Loading)
        try {
            emit(Resource.Success(apiService.getCategories()))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

    override suspend fun insertCategoriesToLocaleDb(categoryList: List<Category>) {
        withContext(Dispatchers.IO){
            categoriesDao.insertAll(categoryList)
        }

    }

}
