package com.andry.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.andry.data.models.Category
import com.andry.data.models.Customer
import com.andry.data.models.Food
import com.andry.data.models.ingredients.IngredientsItem
import com.andry.data.room.daos.CategoriesDao
import com.andry.data.room.daos.CustomerDao
import com.andry.data.room.daos.FoodDao
import com.andry.data.room.daos.IngredientsDao
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Provider

@Database(
    entities = [
        Customer::class,
        Category::class,
        Food::class,
        IngredientsItem::class,
    ],
    version = 6,
    exportSchema = false
)
abstract class KioskDatabase : RoomDatabase() {

    abstract fun customerDao(): CustomerDao
    abstract fun categoryDao(): CategoriesDao
    abstract fun foodDao(): FoodDao
    abstract fun ingredientsDao(): IngredientsDao

    class Callback @Inject constructor(
        private val database: Provider<KioskDatabase>,
        @ApplicationContext private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback()
}
