package com.andry.di

import android.content.Context
import androidx.room.Room
import com.andry.data.local.KioskDatabase
import com.andry.data.room.daos.CategoriesDao
import com.andry.data.room.daos.CustomerDao
import com.andry.data.room.daos.FoodDao
import com.andry.data.room.daos.IngredientsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext appContext: Context,
    ): KioskDatabase {
        return Room.databaseBuilder(appContext, KioskDatabase::class.java, "kiosk_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideCategoriesDao(db: KioskDatabase): CategoriesDao {
        return db.categoryDao()
    }

    @Provides
    fun provideCustomerDao(db: KioskDatabase): CustomerDao {
        return db.customerDao()
    }

    @Provides
    fun provideFoodDao(db: KioskDatabase): FoodDao {
        return db.foodDao()
    }

    @Provides
    fun provideIngredientsDao(db: KioskDatabase): IngredientsDao {
        return db.ingredientsDao()
    }


}