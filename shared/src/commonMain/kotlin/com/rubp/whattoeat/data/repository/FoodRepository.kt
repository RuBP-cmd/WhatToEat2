package com.rubp.whattoeat.data.repository

import kotlinx.coroutines.flow.Flow
import com.rubp.whattoeat.data.local.dao.FoodDao
import com.rubp.whattoeat.data.local.database.AppDatabase
import com.rubp.whattoeat.data.local.entry.Food

class FoodRepository(
    private val dao: FoodDao = AppDatabase.database.foodDao()
) {
    fun getByTableId(tableId: Int): Flow<List<Food>> = dao.getByTableId(tableId)

    fun getAll(): Flow<List<Food>> = dao.getAll()

    suspend fun update(food: Food) = dao.update(food)

    suspend fun insert(food: Food) = dao.insert(food)

    suspend fun delete(food: Food) = dao.delete(food)

    suspend fun deleteByTableId(tableId: Int) = dao.deleteByTableId(tableId)

    suspend fun updateAllMarked(tableId: Int, marked: Boolean) = dao.updateAllMarked(tableId, marked)
}
