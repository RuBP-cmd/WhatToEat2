package com.rubp.whattoeat.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rubp.whattoeat.data.local.entry.Food
import com.rubp.whattoeat.data.local.entry.FoodTable
import com.rubp.whattoeat.data.repository.ConfigRepository
import com.rubp.whattoeat.data.repository.FoodRepository
import com.rubp.whattoeat.data.repository.FoodTableRepository
import com.rubp.whattoeat.domain.selectFood
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class FoodViewModel(
    private val foodRepository: FoodRepository = FoodRepository(),
    private val foodTableRepository: FoodTableRepository = FoodTableRepository(),
    private val configRepository: ConfigRepository = ConfigRepository
) : ViewModel() {

    // 所有表格
    val tables: StateFlow<List<FoodTable>> = foodTableRepository.getAll()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // 当前选中的表格 id，将Flow转为StateFlow
    val currentTableId: StateFlow<Long> = configRepository.savedTableIdFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 1L
    )

    // 随着tableId的变化而变化的：
    // 当前表格中的食物，切换表格时自动更新
    val foods: StateFlow<List<Food>> = currentTableId.flatMapLatest { tableId ->
        foodRepository.getByTableId(tableId)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    var chosenFood: Food? = null

    init {
        // 首次启动无表时自动创建"默认"表格
        viewModelScope.launch {
            if(foodTableRepository.getAll().first().isEmpty()){
                createTable("默认")
            }
        }
    }

    // --- 表格管理 ---

    fun switchTable(tableId: Long) {
        if (tableId == currentTableId.value) return
        updateCurrentTableId(tableId)
        chosenFood = null
    }

    fun createTable(name: String) {
        viewModelScope.launch {
            val newTableId = foodTableRepository.insert(FoodTable(name = name))
            repeat(3) {
                foodRepository.insert(
                    Food(
                        name = "",
                        weight = 1,
                        marked = true,
                        tableId = newTableId
                    )
                )
            }
        }
    }

    fun renameTable(tableId: Long, newName: String) {
        viewModelScope.launch {
            val target = tables.value.find { it.id == tableId } ?: return@launch
            foodTableRepository.update(target.copy(name = newName))
        }
    }

    // 删除表，注意删除的表是正在使用的表的情况，需要将表id切换到剩余的表
    fun deleteTable(tableId: Long) {
        viewModelScope.launch {
            foodRepository.deleteByTableId(tableId)
            foodTableRepository.deleteById(tableId)
            if (currentTableId.value == tableId) {
                // 切换到剩余的表中
                val remaining = tables.value.filter { it.id != tableId }
                if (remaining.isNotEmpty()) {
                    updateCurrentTableId(remaining.first().id)
                }
            }
        }
    }

    // --- 食物 CRUD ---

    fun insert(food: Food) {
        viewModelScope.launch { // ui层不管之table_id，因此移到这里添加
            foodRepository.insert(food.copy(tableId = currentTableId.value))
        }
    }

    fun update(food: Food) {
        viewModelScope.launch {
            foodRepository.update(food)
        }
    }

    fun delete(food: Food) {
        viewModelScope.launch {
            foodRepository.delete(food)
        }
    }

    // --- 随机选择 ---

    fun chosenRandomFood(): String {
        val foodList = foods.value.filter { food ->
            food.marked && food.weight > 0 && food.name.isNotEmpty()
        }

        chosenFood = selectFood(foodList, chosenFood)
        return chosenFood?.name ?: "没有可供选择的食物！"
    }

    fun ignoreChosenFood() {
        chosenFood?.let {
            update(it.copy(marked = false))
        }
    }

    fun clearAllIgnore() {
        viewModelScope.launch {
            foodRepository.updateAllMarked(currentTableId.value, marked = true)
        }
    }
    fun updateCurrentTableId(currentTableId: Long){
        configRepository.saveTableId(currentTableId)
    }
}
