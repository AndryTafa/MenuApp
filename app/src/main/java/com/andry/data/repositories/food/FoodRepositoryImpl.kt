package com.andry.data.repositories.food

import com.andry.data.models.Food
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import com.andry.data.api.ApiService
import com.andry.data.room.daos.FoodDao
import com.andry.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

@Singleton
class FoodRepositoryImpl @Inject constructor(
    private val foodDao: FoodDao,
    private val apiService: ApiService,
) :
    FoodRepository {

    override val allFoods: Flow<List<Food>>
        get() = foodDao.getAll().flowOn(Dispatchers.IO)

    override suspend fun getFoodsFromDatabase(): Flow<Resource<List<Food>>> = flow{
        emit(Resource.Loading)
        try {
            emit(Resource.Success(apiService.getFood()))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

    override suspend fun insertAllFoodsToLocaleDb(foodList: List<Food>) {
        println("list" + foodList)
        withContext(Dispatchers.IO){
            foodDao.insertAll(foodList)
        }
    }


}
