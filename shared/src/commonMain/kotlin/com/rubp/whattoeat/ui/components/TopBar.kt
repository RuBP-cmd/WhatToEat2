package com.rubp.whattoeat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import com.composables.icons.materialicons.MaterialIcons
import com.composables.icons.materialicons.filled.Chevron_left
import com.composables.icons.materialicons.filled.More_vert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun AppTopBar(
    onClickReturn: (() -> Unit),
    title: String? = null,
    onClickMore: (() -> Unit)? = null
){
    val componentColor = MaterialTheme.colorScheme.onPrimary
    Row(
        modifier = Modifier.fillMaxWidth().background(color = MaterialTheme.colorScheme.primary),
        horizontalArrangement = Arrangement.SpaceBetween, // 左边的放最左，右边的放最右，其余均分排布
        verticalAlignment = Alignment.CenterVertically
    ){
        IconButton(onClickReturn){
            Icon(
                imageVector = MaterialIcons.Filled.Chevron_left,
                contentDescription = "返回",
                tint = componentColor
            )
        }

        title?.let{
            Text(
                text = it,
                color = componentColor,
                style = MaterialTheme.typography.titleLarge
            )
        }


        if(onClickMore == null) {
            IconButton( // 仅作为占位
                onClick = {},
                enabled = false
            ) {}
        } else {
            IconButton(onClickMore){
                Icon(
                    imageVector = MaterialIcons.Filled.More_vert,
                    contentDescription = "更多",
                    tint = componentColor
                )
            }
        }

    }
}

@Preview
@Composable
private fun TopBarPreview(){
    AppTopBar({}, "title", {})
}