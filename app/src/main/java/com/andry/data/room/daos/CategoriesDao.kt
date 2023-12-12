package com.andry.data.room.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.andry.data.models.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriesDao {
    @Query("SELECT * FROM Category")
    fun getAll(): Flow<List<Category>>

    @Query("SELECT * FROM Category WHERE categoryId LIKE :id LIMIT 1")
    fun getCategoryById(id: Int): Category

    @Query("DELETE FROM Category")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(users: List<Category>)

    @Delete
    fun delete(user: Category)
}