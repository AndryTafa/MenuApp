package com.andry.di

import com.andry.data.api.ApiService
import com.andry.data.repositories.categories.CategoryRepository
import com.andry.data.repositories.categories.CategoryRepositoryImpl
import com.andry.data.repositories.food.FoodRepository
import com.andry.data.repositories.food.FoodRepositoryImpl
import com.andry.data.repositories.ingredients.IngredientsRepository
import com.andry.data.repositories.ingredients.IngredientsRepositoryImpl
import com.andry.data.room.daos.CategoriesDao
import com.andry.data.room.daos.FoodDao
import com.andry.data.room.daos.IngredientsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideCategoryRepository(
        categoriesDao: CategoriesDao,
        apiService: ApiService,
    ): CategoryRepository {
        return CategoryRepositoryImpl(categoriesDao, apiService)
    }

    @Provides
    @Singleton
    fun provideFoodRepository(
        foodDao: FoodDao,
        apiService: ApiService,
    ): FoodRepository {
        return FoodRepositoryImpl(foodDao, apiService)
    }

    @Provides
    @Singleton
    fun provideIngredientsRepository(
        ingredientsDao: IngredientsDao,
        apiService: ApiService,
    ): IngredientsRepository {
        return IngredientsRepositoryImpl(ingredientsDao, apiService)
    }


}