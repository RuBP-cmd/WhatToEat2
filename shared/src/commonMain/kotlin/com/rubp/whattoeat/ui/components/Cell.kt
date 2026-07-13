package com.rubp.whattoeat.ui.components

import androidx.compose.runtime.Composable

class Cell(
    val content: @Composable () -> Unit,
    val weight: Float
)
