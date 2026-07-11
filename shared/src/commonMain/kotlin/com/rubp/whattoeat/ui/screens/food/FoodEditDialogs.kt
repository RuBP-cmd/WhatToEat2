package com.rubp.whattoeat.ui.screens.food

import androidx.compose.runtime.Composable
import com.rubp.whattoeat.data.local.entry.FoodTable
import com.rubp.whattoeat.ui.components.ConfirmDialog
import com.rubp.whattoeat.ui.components.EditDialog

@Composable
fun FoodEditDialogHandler(
    dialogState: EditDialogState,
    tableName: String,
    currentTable: FoodTable?,
    actions: FoodEditActions,
    onDismiss: () -> Unit
) {
    when(dialogState){
        // 无对话框
        is EditDialogState.None -> return

        // 弹出创建新表格对话框
        is EditDialogState.CreateTable -> {
            EditDialog(
                title = "新建表格",
                labelText = "表格名称",
                onConfirm = { name ->
                    actions.onCreateTable(name)
                    onDismiss()
                },
                onDismiss = onDismiss
            )
        }

        // 弹出修改表格对话框
        is EditDialogState.RenameTable -> {
            EditDialog(
                title = "修改表格名称",
                initialText = tableName,
                labelText = "新的表格名称",
                onConfirm = { newName ->
                    currentTable?.let { actions.onRenameTable(it.id, newName) }
                    onDismiss()
                },
                onDismiss = onDismiss
            )
        }

        // 弹出删除表格对话框
        is EditDialogState.DeleteTable -> {
            ConfirmDialog(
                title = "删除表格",
                message = "确认删除？",
                onConfirm = {
                    currentTable?.id?.let { actions.onDeleteTable(it) }
                    onDismiss()
                },
                onDismiss = onDismiss
            )
        }

        is EditDialogState.DeleteFood -> {
            ConfirmDialog(
                title = "删除菜品",
                message = "确认删除？",
                onConfirm = {
                    actions.onDelFood(dialogState.food)
                    onDismiss()
                },
                onDismiss = onDismiss
            )
        }
    }
}