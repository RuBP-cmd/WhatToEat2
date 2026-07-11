package com.rubp.whattoeat.ui.screens.food

import com.rubp.whattoeat.data.local.entry.Food
import com.rubp.whattoeat.domain.FoodTableDto

interface FoodEditActions {
    fun onReturnToEat()
    fun onTableSelected(id: Long)
    fun onRenameTable(tableId: Long, name: String)
    fun onDeleteTable(tableId: Long)
    fun onCreateTable(name: String)
    fun onImportFoodAndTable(dto: FoodTableDto)
    fun onAddFood()
    fun onDelFood(food: Food)
    fun onClickStar(food: Food)
    fun onInputName(food: Food, name: String)
    fun onInputWeight(food: Food, weight: Int)
}

sealed class EditDialogState {
    data object None : EditDialogState()
    data object CreateTable : EditDialogState()
    data object RenameTable : EditDialogState()
    data object DeleteTable : EditDialogState()
    data class DeleteFood(val food: Food) : EditDialogState()
}