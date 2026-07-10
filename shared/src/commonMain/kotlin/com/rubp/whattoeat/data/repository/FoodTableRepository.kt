package com.rubp.whattoeat.data.repository

import kotlinx.coroutines.flow.Flow
import com.rubp.whattoeat.data.local.dao.FoodTableDao
import com.rubp.whattoeat.data.local.database.AppDatabase
import com.rubp.whattoeat.data.local.entry.FoodTable

class FoodTableRepository(
    private val dao: FoodTableDao = AppDatabase.database.foodTableDao()
) {
    fun getAll(): Flow<List<FoodTable>> = dao.getAll()

    suspend fun insert(table: FoodTable): Long = dao.insert(table)

    suspend fun update(table: FoodTable) = dao.update(table)

    suspend fun delete(table: FoodTable) = dao.delete(table)

    suspend fun deleteById(tableId: Long) = dao.deleteById(tableId)
}
