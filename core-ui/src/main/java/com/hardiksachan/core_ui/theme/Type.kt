package com.hardiksachan.core_ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.hardiksachan.core_ui.R

internal val DMSansFamily = FontFamily(
    listOf(
        Font(R.font.dm_sans_regular),
        Font(R.font.dm_sans_medium, weight = FontWeight.Medium),
        Font(R.font.dm_sans_bold, weight = FontWeight.Bold),
    )
)

// Set of Material typography styles to start with
internal val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = DMSansFamily,
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 36.sp
    ),
    displayMedium = TextStyle(
        fontFamily = DMSansFamily,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 36.sp
    ),
    titleLarge = TextStyle(
        fontFamily = DMSansFamily,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 32.sp
    ),
    titleMedium = TextStyle(
        fontFamily = DMSansFamily,
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 24.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = DMSansFamily,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = DMSansFamily,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 20.sp
    ),
    labelLarge = TextStyle(
        fontFamily = DMSansFamily,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 14.sp
    ),
    labelMedium = TextStyle(
        fontFamily = DMSansFamily,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 12.sp
    ),
)