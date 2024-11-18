package com.mlf.kdndapp.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val clNone  = Color(0x00000000)
val clBlack = Color(0xFF000000)
val clGray1 = Color(0xFF606060)
val clGray2 = Color(0xFFC0C0C0)
val clGray3 = Color(0xFFE0E0E0)
val clGray4 = Color(0xFFFAFAFA)
val clWhite = Color(0xFFFFFFFF)

val clRed1 = Color(0xFF540808)
val clRed2 = Color(0xFF6e0b0b)
val clRed3 = Color(0xFF6e0b0b)
val clRed4 = Color(0xFFa61111)
val clRed5 = Color(0xFFcc1414)

val clGreen1 = Color(0xFF053305)
val clGreen2 = Color(0xFF064206)
val clGreen3 = Color(0xFF09560a)
val clGreen4 = Color(0xFF0b730e)
val clGreen5 = Color(0xFF0f9612)

val clBlue1 = Color(0xFF01214d)
val clBlue2 = Color(0xFF092f66)
val clBlue3 = Color(0xFF0f3e7f)
val clBlue4 = Color(0xFF1c55a6)
val clBlue5 = Color(0xFF3d74bf)

val clYellow1 = Color(0xFF4d4d00)
val clYellow2 = Color(0xFF666600)
val clYellow3 = Color(0xFF8c8c00)
val clYellow4 = Color(0xFFcccc00)
val clYellow5 = Color(0XFFf2f200)

val clBrown1 = Color(0xFF332217)
val clBrown2 = Color(0xFF533825)
val clBrown3 = Color(0xFF734e34)
val clBrown4 = Color(0xFF996744)
val clBrown5 = Color(0xFFcc895b)

val clPrimary = clBrown3
val clSecondary = clBrown4
val clTertiary = clBrown5

val dimMainPadding = 16.dp

// Spacer
val dimSpaceH = 8.dp
val dimSpaceV = 2.dp

// Botones
val clButBrownEnBg = clBrown3
val clButBrownEnText = clYellow5
val clButBrownEnStroke = clBrown2
val clButBrownDisBg = clBrown4
val clButBrownDisText = clYellow4
val clButBrownDisStroke = clBrown4

val dimButFont = 18.sp
val dimButStroke = 2.dp

// Edits
val clEdEnBg = clBrown2
val clEdEnText = clYellow5
val clEdDisText = clYellow3
val clEdDisStroke = clYellow3
val clEdNormalStroke = clYellow4
val clEdFocusStroke = clYellow5
val clEdCursor = clYellow5

val dimEdFocusStroke = 4.dp
val dimEdNormalStroke = 2.dp
val dimTextFont = 18.sp

val controlShape = RoundedCornerShape(percent = 25)
val edCursorBrush = SolidColor(clEdCursor)

// Características
val clStrength = Color(0xFFFF4C4C)
val clDexterity = Color(0xFFB366FF)
val clConstitution = Color(0xFF33FF33)
val clIntelligence = Color(0xFF80c0ff)
val clWisdom = Color(0xFFE5E500)
val clCharisma = Color(0xFFE57300)

// Contenedor
val clContentEnStroke = Color(0xFFA58331)
val clContentEnBg = Color(0xFF1A1F21)
val clContentEnText = clWhite
val clContentDisStroke = clGray3
val clContentDisBg = Color(0xFF1A1F21)
val clContentDisText = clGray3
val clContentFocusStroke = clYellow4
val clContentFocusBg = Color(0xFF1A1F21)
val clContentFocusText = clWhite
