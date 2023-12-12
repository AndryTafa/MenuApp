package com.andry.data.room.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.andry.data.models.Food
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Query("SELECT * FROM Food")
    fun getAll(): Flow<List<Food>>

    @Query("SELECT * FROM Food WHERE categoryId LIKE :catId LIMIT 1")
    fun getFoodByCategoryId(catId: Int): Food

    @Query("DELETE FROM Food")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(foodList: List<Food>)

    @Delete
    fun delete(user: Food)
}