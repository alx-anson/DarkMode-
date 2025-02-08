package com.anson.darkmodeplus

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val CompactTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.helvetica_neue_medium)),
        fontSize = 12.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight(500)
    ),
    titleLarge = TextStyle(
        fontSize = 24.sp,
        lineHeight = 32.sp,
        fontFamily = FontFamily(Font(R.font.helvetica_neue_medium)),
        fontWeight = FontWeight(500)
    ),
    titleMedium = TextStyle(
        fontSize = 20.sp,
        lineHeight = 32.sp,
        fontFamily = FontFamily(Font(R.font.helvetica_neue_medium)),
        fontWeight = FontWeight(500)
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight(500),
        fontFamily = FontFamily(Font(R.font.helvetica_neue_medium)),
        fontSize = 18.sp,
        lineHeight = 24.sp,
    ),

    labelMedium = TextStyle(
        fontWeight = FontWeight(700),
        fontFamily = FontFamily(Font(R.font.helvetica_neue_medium)),
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),

    displayMedium = TextStyle(
        fontWeight = FontWeight(500),
        fontFamily = FontFamily(Font(R.font.helvetica_neue_medium)),
        fontSize = 15.sp,
        lineHeight = 20.sp,
    ),
)

val CompactSmallTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.helvetica_neue_medium)),
        fontSize = 10.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight(500)
    ),
    titleLarge = TextStyle(
        fontSize = 20.sp,
        lineHeight = 32.sp,
        fontFamily = FontFamily(Font(R.font.helvetica_neue_medium)),
        fontWeight = FontWeight(500)
    ),
    titleMedium = TextStyle(
        fontSize = 16.sp,
        lineHeight = 32.sp,
        fontFamily = FontFamily(Font(R.font.helvetica_neue_medium)),
        fontWeight = FontWeight(500)
    ),

    displayMedium = TextStyle(
        fontWeight = FontWeight(500),
        fontFamily = FontFamily(Font(R.font.helvetica_neue_medium)),
        fontSize = 13.sp,
        lineHeight = 17.sp,
    ),
)