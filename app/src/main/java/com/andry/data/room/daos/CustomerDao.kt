package com.andry.data.room.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.andry.data.models.Customer

@Dao
interface CustomerDao {
    @Query("SELECT * FROM customer")
    fun getAll(): List<Customer>

    @Query("SELECT * FROM customer WHERE email LIKE :customerEmail LIMIT 1")
    fun findByEmail(customerEmail: String): Customer

    @Query("DELETE FROM customer")
    fun deleteAll()

    @Insert
    fun insertAll(users: List<Customer>)

    @Delete
    fun delete(user: Customer)
}