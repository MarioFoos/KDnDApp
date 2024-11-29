package com.mlf.kdndapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun fontSizeResourse(id: Int) : TextUnit = dimensionResource(id).value.sp

@Composable
fun pxToDp(px: Int) : Dp = (px.toFloat()/LocalDensity.current.density).dp

@Composable
fun pxToDp(px: Float) : Dp = (px/LocalDensity.current.density).dp

@Composable
fun dpToPx(dp: Dp) : Int = (dp.value*LocalDensity.current.density).toInt()

@Composable
fun pxToSp(px: Int) : TextUnit = (px.toFloat()/LocalDensity.current.fontScale).sp

@Composable
fun pxToSp(px: Float) : TextUnit = (px/LocalDensity.current.fontScale).sp

@Composable
fun spToPx(sp: TextUnit) : Int = (sp.value*LocalDensity.current.fontScale).toInt()

@Composable
fun dpToSp(dp: Dp) : TextUnit = (dp.value*LocalDensity.current.density/LocalDensity.current.fontScale).sp
