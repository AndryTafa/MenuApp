package com.andry.data.repositories.ingredients

import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton
import com.andry.data.api.ApiService
import com.andry.data.models.ingredients.IngredientsItem
import com.andry.data.room.daos.IngredientsDao
import com.andry.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

@Singleton
class IngredientsRepositoryImpl @Inject constructor(
    private val ingredientsDao: IngredientsDao,
    private val apiService: ApiService,
) :
    IngredientsRepository {

    override val allIngredients: Flow<List<IngredientsItem>>
        get() = ingredientsDao.getAll().flowOn(Dispatchers.IO)

    override suspend fun getIngredientsFromDatabase(): Flow<Resource<List<IngredientsItem>>> = flow{
        emit(Resource.Loading)
        try {
            emit(Resource.Success(apiService.getIngredients()))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

    override suspend fun insertAllIngredientsToLocaleDb(ingredientsList: List<IngredientsItem>) {
        withContext(Dispatchers.IO){
            ingredientsDao.insertAll(ingredientsList)
        }
    }


}
