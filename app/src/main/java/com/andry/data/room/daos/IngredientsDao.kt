package com.andry.data.room.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.andry.data.models.ingredients.IngredientsItem
import kotlinx.coroutines.flow.Flow

@Dao
interface IngredientsDao {
    @Query("SELECT * FROM IngredientsItem")
    fun getAll(): Flow<List<IngredientsItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(ingredientsList: List<IngredientsItem>)

}