package com.andry.data.room.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.andry.data.models.Category
import com.andry.data.models.Customer
import com.andry.data.models.Food
import com.andry.data.models.ingredients.IngredientsItem
import com.andry.data.room.daos.CategoriesDao
import com.andry.data.room.daos.CustomerDao
import com.andry.data.room.daos.FoodDao
import com.andry.data.room.daos.IngredientsDao

@Database(
    entities = [
        Customer::class,
        Category::class,
        Food::class,
        IngredientsItem::class
    ],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun customerDao(): CustomerDao
    abstract fun categoryDao(): CategoriesDao
    abstract fun foodDao(): FoodDao
    abstract fun ingredientsDao(): IngredientsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
