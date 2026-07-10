package com.rubp.whattoeat.ui.screens.food

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import com.composables.icons.materialicons.MaterialIcons
import com.composables.icons.materialicons.filled.Add
import com.composables.icons.materialicons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rubp.whattoeat.data.local.entry.Food
import com.rubp.whattoeat.data.local.entry.FoodTable
import com.rubp.whattoeat.model.Cell
import com.rubp.whattoeat.ui.components.AppTopBar
import com.rubp.whattoeat.ui.components.CardText
import com.rubp.whattoeat.ui.components.CardTextFiled
import com.rubp.whattoeat.ui.components.CircleIconButton
import com.rubp.whattoeat.ui.components.ConfirmDialog
import com.rubp.whattoeat.ui.components.LeftSwipeBox
import com.rubp.whattoeat.ui.components.PrimaryButton
import com.rubp.whattoeat.ui.components.RowItem
import com.rubp.whattoeat.ui.viewmodel.FoodViewModel
import org.jetbrains.compose.resources.painterResource
import whattoeat.shared.generated.resources.Res
import whattoeat.shared.generated.resources.filled_star
import whattoeat.shared.generated.resources.outlined_star

@Composable
fun FoodEditScreen(
    foodViewModel: FoodViewModel,
    onReturnToEat: () -> Unit
) {
    val foodList by foodViewModel.foods.collectAsState(initial = emptyList())
    val tables by foodViewModel.tables.collectAsState()
    val currentTableId by foodViewModel.currentTableId.collectAsState()

    FoodEditContent(
        foodList = foodList,
        tables = tables,
        currentTableId = currentTableId,
        onReturnToEat = onReturnToEat,
        onTableSelected = { foodViewModel.switchTable(it) },
        onRenameTable = { tableId, name -> foodViewModel.renameTable(tableId, name) },
        onDeleteTable = { tableId -> foodViewModel.deleteTable(tableId) },
        onCreateTable = { name -> foodViewModel.createTable(name) },
        onAddFood = { foodViewModel.insert(Food(name = "", weight = 1, marked = true)) },
        onDelFood = { food -> foodViewModel.delete(food) },
        onClickStar = { food -> foodViewModel.update(food.copy(marked = !food.marked)) },
        onInputName = { food, name -> foodViewModel.update(food.copy(name = name)) },
        onInputWeight = { food, weight -> foodViewModel.update(food.copy(weight = weight)) }
    )
}

@Composable
fun FoodEditContent(
    foodList: List<Food>,
    tables: List<FoodTable>,
    currentTableId: Long,
    onReturnToEat: () -> Unit,
    onTableSelected: (Long) -> Unit,
    onRenameTable: (Long, String) -> Unit,
    onDeleteTable: (Long) -> Unit,
    onCreateTable: (String) -> Unit,
    onAddFood: () -> Unit,
    onDelFood: (food: Food) -> Unit,
    onClickStar: (food: Food) -> Unit,
    onInputName: (food: Food, name: String) -> Unit,
    onInputWeight: (food: Food, weight: Int) -> Unit
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var showDeleteTableDialog by remember { mutableStateOf(false) }
    var showDeleteFoodDialog by remember { mutableStateOf(false) }
    var newTableName by remember { mutableStateOf("") }
    val tableName = tables.find{ table -> table.id == currentTableId }?.name ?: ""
    var foodPendingDelete by remember { mutableStateOf<Food?>(null) }

    Scaffold(
        topBar = {
            AppTopBar(onReturnToEat, "编辑清单")
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 5.dp, bottom = 8.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ){
                // 标题
                CardText(
                    modifier = Modifier
                        .padding(horizontal = 35.dp)
                        .height(70.dp),
                    text = tableName,
                    style = MaterialTheme.typography.titleLarge,
                    textColor = MaterialTheme.colorScheme.primary
                )

                // 滚动标题栏（切换表格）
                ScrollableTableTitleRow(
                    modifier = Modifier
                        .padding(horizontal = 30.dp)
                        .fillMaxWidth(),
                    currentTableId = currentTableId,
                    tables = tables,
                    onTableSelected = onTableSelected,
                    onAddTable = { showCreateDialog = true } // 拉出创建对话框
                )
                
                // 编辑表
                EditTable(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 30.dp),
                    foodList = foodList,
                    onClickStar = onClickStar,
                    onInputName = onInputName,
                    onInputWeight = onInputWeight,
                    onClickDelFood = { foodPendingDelete = it; showDeleteFoodDialog = true },
                )

                Spacer(Modifier.height(25.dp))
                // 底部按钮
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                ){
                    PrimaryButton("添加新菜品", Modifier
                        .weight(2f)
                        .height(48.dp)) { onAddFood() }
                    PrimaryButton(
                        "保存",
                        Modifier
                            .weight(1f)
                            .height(48.dp),
                        MaterialTheme.colorScheme.surfaceVariant,
                        MaterialTheme.colorScheme.primary)
                    {  }
                }
            }

        }
    }

    // 弹出对话框
    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = {
                showCreateDialog = false
                newTableName = ""
            },
            title = { Text("新建表格") },
            text = {
                OutlinedTextField(
                    value = newTableName,
                    onValueChange = { newTableName = it },
                    label = { Text("表格名称") },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onCreateTable(newTableName.trim())
                        newTableName = ""
                        showCreateDialog = false
                    },
                    enabled = newTableName.isNotBlank()
                ) { Text("创建") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showCreateDialog = false
                    newTableName = ""
                }) { Text(text = "取消", color = MaterialTheme.colorScheme.onSurface) }
            }
        )
    }

    if(showDeleteTableDialog) {
        ConfirmDialog(
            title = "删除表格",
            message = "确认删除？",
            onConfirm = {
                onDeleteTable(currentTableId)
                showDeleteTableDialog = false
            },
            onDismiss = {
                showDeleteTableDialog = false
            }
        )
    }
    if(showDeleteFoodDialog) {
        ConfirmDialog(
            title = "删除菜品",
            message = "确认删除？",
            onConfirm = {
                foodPendingDelete?.let{
                    onDelFood(it)
                }
                showDeleteFoodDialog = false
            },
            onDismiss = {
                showDeleteFoodDialog = false
            }
        )
    }
}


@Composable
private fun ScrollableTableTitleRow(
    modifier: Modifier,
    currentTableId: Long, // 更新的根源
    tables: List<FoodTable>,
    onTableSelected: (Long) -> Unit,
    onAddTable: () -> Unit,
){
    val selectedIndex = remember(currentTableId, tables) {
        tables.indexOfFirst{ it.id == currentTableId }
    }
    PrimaryScrollableTabRow(
        modifier = modifier,
        selectedTabIndex = selectedIndex
    ) {
        tables.forEachIndexed { index, table ->
            Tab(
                selected = index == selectedIndex,
                onClick = { onTableSelected(table.id) },
                text = {
                    Text(
                        text = table.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            )
        }

        Tab(
            selected = false,
            onClick = onAddTable,
            text = {
                Text(
                    text = "添加表格",
                    style = MaterialTheme.typography.titleSmall
                )
            },
            icon = {
                Icon(
                    imageVector = MaterialIcons.Filled.Add,
                    contentDescription = "添加表格"
                )
            }
        )
    }
}

@Composable
private fun EditTable(
    modifier: Modifier,
    foodList: List<Food>,
    onClickStar: (food: Food) -> Unit,
    onInputName: (food: Food, name: String) -> Unit,
    onInputWeight: (food: Food, weight: Int) -> Unit,
    onClickDelFood: (food: Food) -> Unit,
) {
    val weightList = listOf(2f, 8f, 3f)
    val titleColor = MaterialTheme.colorScheme.primary
    val titleStyle = MaterialTheme.typography.titleMedium


    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        stickyHeader {
            RowItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 5.dp),
                cells = listOf(
                    Cell({ Text(text = "参选", color = titleColor, style = titleStyle) }, weightList[0]),
                    Cell({ Text(text = "名称", color = titleColor, style = titleStyle) }, weightList[1]),
                    Cell({ Text(text = "权重", color = titleColor, style = titleStyle) }, weightList[2])
                )
            )
        }

        items(foodList, key = { it.id }) { food ->
            SwipeRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(horizontal = 2.dp)
                    .animateItem(),
                food = food,
                weightList = weightList,
                onClickStar = onClickStar,
                onInputName = onInputName,
                onInputWeight = onInputWeight,
                onDelete = { onClickDelFood(food) }
            )
        }
    }
}

@Composable
private fun SwipeRow(
    modifier: Modifier,
    food: Food,
    weightList: List<Float>,
    onClickStar: (food: Food) -> Unit,
    onInputName: (food: Food, name: String) -> Unit,
    onInputWeight: (food: Food, weight: Int) -> Unit,
    onDelete: () -> Unit
) {
    var isError by remember { mutableStateOf(false) }
    var foodName by remember(food.id) { mutableStateOf(TextFieldValue(food.name)) }
    var foodWeight by remember(food.id) { mutableStateOf(TextFieldValue(food.weight.toString())) }
    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent,
        errorContainerColor = Color.Transparent,
    )

    LeftSwipeBox(
        menuWidthDp = 60.dp,
        modifier = modifier,
        menuContent = {
            CircleIconButton(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(end = 3.dp),
                backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                onClick = onDelete
            ) {
                Icon(
                    imageVector = (MaterialIcons.Filled.Delete),
                    contentDescription = "删除此行数据"
                )
            }
        }
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
            elevation = CardDefaults.elevatedCardElevation(),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
        ){
            RowItem(
                modifier = Modifier.fillMaxSize(),
                cells = listOf(
                    Cell( // 参选Star
                        content = {
                            IconButton({ onClickStar(food) }) {
                                Image(
                                    painter = painterResource(
                                        if (food.marked) Res.drawable.filled_star
                                        else Res.drawable.outlined_star
                                    ),
                                    contentDescription = "参选"
                                )
                            }
                        },
                        weight = weightList[0]
                    ),
                    Cell( // 食物名称
                        content = {
                            Surface(
                                color = MaterialTheme.colorScheme.surfaceContainer,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                OutlinedTextField(
                                    value = foodName,
                                    textStyle = MaterialTheme.typography.bodyMedium,
                                    colors = textFieldColors,
                                    placeholder = {
                                        Text(
                                            text = "请输入名称",
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    },
                                    onValueChange = { newFoodName ->
                                        foodName = newFoodName
                                        onInputName(food, newFoodName.text)
                                    }
                                )
                            }
                        },
                        weight = weightList[1]
                    ),
                    Cell( // 权重
                        content = {
                            Surface(
                                color = MaterialTheme.colorScheme.surfaceContainer,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                OutlinedTextField(
                                    value = foodWeight,
                                    textStyle = MaterialTheme.typography.bodyMedium,
                                    colors = textFieldColors,
                                    onValueChange = { newFoodWeight ->
                                        foodWeight = newFoodWeight
                                        val text = newFoodWeight.text
                                        if (text.isNotEmpty() && text.length <= 5 && text.all { it.isDigit() }) {
                                            onInputWeight(food, text.toInt())
                                            isError = false
                                        } else {
                                            isError = text.isNotEmpty()
                                        }
                                    },
                                    isError = isError,
                                    supportingText = if (isError) {
                                        { Text("请输入有效数字", color = MaterialTheme.colorScheme.error) }
                                    } else null,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                )
                            }
                        },
                        weight = weightList[2]
                    )
                )
            )
        }

    }
}

@Preview
@Composable
private fun FoodEditContentPreview() {
    FoodEditContent(
        foodList = emptyList(),
        tables = listOf(
            FoodTable(1L, "早餐", 0L),
            FoodTable(2L, "午餐", 1L)
        ),
        currentTableId = 1L,
        onReturnToEat = {},
        onTableSelected = {},
        onRenameTable = { _, _ -> },
        onDeleteTable = {},
        onCreateTable = {},
        onAddFood = {},
        onDelFood = {},
        onClickStar = { },
        onInputName = { _, _ -> },
        onInputWeight = { _, _ -> },
    )
}
