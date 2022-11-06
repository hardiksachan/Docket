package com.hardiksachan.core_ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hardiksachan.core_ui.Grey

@Composable
fun StrokedRow(
    modifier: Modifier = Modifier, content: @Composable () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(
            color = Grey.Dark,
            thickness = 1.dp,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(4.dp))
        content()
        Spacer(modifier = Modifier.width(4.dp))
        Divider(
            color = Grey.Dark,
            thickness = 1.dp,
            modifier = Modifier.weight(1f)
        )
    }
}