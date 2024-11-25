package com.mlf.kdndapp

import android.content.Intent
import android.content.res.AssetManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dndlib.DNDHeight
import com.dndlib.DNDWeight
import com.dndlib.base.EAbility
import com.dndlib.base.EAlignment
import com.dndlib.base.EClass
import com.dndlib.base.EEthnicity
import com.dndlib.base.EFeat
import com.dndlib.base.EGender
import com.dndlib.base.ERace
import com.dndlib.base.EStageOfLife
import com.dndlib.res.Res
import com.mlf.kdndapp.ui.theme.KDnDAppTheme
import kotlin.system.exitProcess

val APP_TAG = "AppTag"
val controlShape = RoundedCornerShape(percent = 25)

class MainActivity : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        // Configurar librería
        if(!configLibrary(assets))
        {
            exitProcess(-1)
        }
        showCharInfo()

        // Arrays
        val arrRace : ArrayList<Map.Entry<ERace, String>> = Res.getLocale(ERace.values())
        val arrEthnic : ArrayList<Map.Entry<EEthnicity, String>> = Res.getLocale(EEthnicity.values())
        val arrClass : ArrayList<Map.Entry<EClass, String>> = Res.getLocale(EClass.values())
        val arrAlign : ArrayList<Map.Entry<EAlignment, String>> = Res.getLocale(EAlignment.values())
        val arrGender : ArrayList<Map.Entry<EGender, String>> = Res.getLocale(EGender.values())
        val arrStage : ArrayList<Map.Entry<EStageOfLife, String>> = Res.getLocale(EStageOfLife.values())
        val arrFeat : ArrayList<Map.Entry<EFeat, String>> = Res.getLocale(EFeat.values())
        val arrAbility : ArrayList<Map.Entry<EAbility, String>> = Res.getLocale(EAbility.values())

        // Valores iniciales
        val raceInit : Int = Res.randomIndex(arrRace)
        val ethnicInit : Int = Res.randomIndex(arrEthnic)
        val classInit : Int = Res.randomIndex(arrClass)
        val alignInit : Int = Res.randomIndex(arrAlign)
        val genderInit : Int  = Res.randomIndex(arrGender)
        val stageInit : Int = Res.randomIndex(arrStage)
        val featInit : Int = Res.randomIndex(arrFeat)
        val ability1Init : Int = Res.randomIndex(arrAbility)
        val ability2Init : Int = Res.randomIndex(arrAbility)
        setContent {
            KDnDAppTheme {
                var name by rememberSaveable { mutableStateOf("") }
                var race by rememberSaveable { mutableStateOf(arrRace[raceInit].key) }
                var ethnic by rememberSaveable { mutableStateOf(arrEthnic[ethnicInit].key) }
                var gender by rememberSaveable { mutableStateOf(arrGender[genderInit].key) }
                var klass by rememberSaveable { mutableStateOf(arrClass[classInit].key) }
                var align by rememberSaveable { mutableStateOf(arrAlign[alignInit].key) }
                var stage by rememberSaveable { mutableStateOf(arrStage[stageInit].key) }
                var age by rememberSaveable { mutableStateOf(ERace.genAge(race, stage)) }
                var height by rememberSaveable { mutableStateOf(ERace.genHeight(race)) }
                var weight by rememberSaveable { mutableStateOf(ERace.genWeight(race)) }
                var feat by rememberSaveable { mutableStateOf(arrFeat[featInit].key) }
                var ability1 by rememberSaveable { mutableStateOf(arrAbility[ability1Init].key) }
                var ability2 by rememberSaveable { mutableStateOf(arrAbility[ability2Init].key) }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .paint(
                            painterResource(id = R.drawable.bg_hoja_dnd),
                            contentScale = ContentScale.FillBounds
                        )
                   ){}
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                        .padding(
                            dimensionResource(R.dimen.dimMainPadding)
                        )
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    // Nombre
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1.8f, true)) {
                            ShowEditText(text = name, onValueChange = { name = it })
                        }
                        SpaceH()
                        Column(Modifier.weight(0.2f, true)) {
                            ShowButton(icon = R.drawable.d20, onItemClick = { name = Res.genName(race, ethnic, gender, stage)  })
                        }
                    }
                    SpaceV()
                    // Raza y género
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1.4f, true)) {
                            ShowDropDown(arrRace, initIndex = raceInit, onItemClick = {
                                race = arrRace[it].key
                                age = ERace.genAge(race, stage)
                                height = ERace.genHeight(race)
                                weight = ERace.genWeight(race) })
                        }
                        SpaceH()
                        Column(Modifier.weight(0.6f, true)) {
                            ShowDropDown(arrGender, initIndex = genderInit, onItemClick = { gender = arrGender[it].key })
                        }
                    }
                    SpaceV()
                    // Étnia
                    if(race == ERace.VARIANT_HUMAN || race == ERace.HUMAN) {
                        ShowDropDown(arrEthnic, initIndex = ethnicInit, onItemClick = { ethnic = arrEthnic[it].key })
                        SpaceV()
                        Text(text = Res.getLocale(ethnic, "desc"),
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = dimensionResource(R.dimen.fontText).value.sp,
                        )
                        SpaceV()
                    }
                    // Edad
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Edad
                        Column(Modifier.weight(1f, true)) {
                            ShowButton(text = age.toString() + " " + Res.getLocale("years"), icon = R.drawable.d20, onItemClick = { age = ERace.genAge(race, stage)  })
                        }
                        SpaceH()
                        // Estadío
                        Column(Modifier.weight(1f, true)) {
                            ShowDropDown(arrStage, initIndex = stageInit, onItemClick = {
                                stage = arrStage[it].key
                                age = ERace.genAge(race, stage) }
                            )
                        }
                    }
                    SpaceV()
                    // Altura y Peso
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f, true)) {
                            ShowButton(text = DNDHeight.toString(height), icon = R.drawable.d20, onItemClick = { height = ERace.genHeight(race) })
                        }
                        SpaceH()
                        Column(Modifier.weight(1f, true)) {
                            ShowButton(text = DNDWeight.toString(weight), icon = R.drawable.d20, onItemClick = { weight = ERace.genWeight(race) })
                        }
                    }
                    SpaceV()
                    // Alineación
                    ShowDropDown(arrAlign, initIndex = alignInit, onItemClick = { align = arrAlign[it].key }, true)
                    SpaceV()
                    // Características
                    ShowAbilitiesForRace(race = race)
                    SpaceV()
                    if(race == ERace.VARIANT_HUMAN)
                    {
                        ShowDropDown(arrFeat, initIndex = featInit, onItemClick = { feat = arrFeat[it].key })
                        SpaceV()
                        ShowFeat(feat = feat)
                        SpaceV()
                    }
                    if(race == ERace.HALFELF || race == ERace.VARIANT_HUMAN)
                    {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f, true)) {
                                ShowDropDown(arrAbility, initIndex = ability1Init, onItemClick = { ability1 = arrAbility[it].key })
                            }
                            SpaceH()
                            Column(Modifier.weight(1f, true)) {
                                ShowDropDown(arrAbility, initIndex = ability2Init, onItemClick = { ability2 =  arrAbility[it].key })
                            }
                        }
                        SpaceV()
                    }
                    // Clase
                    ShowDropDown(arrClass, initIndex = classInit, onItemClick = { klass = arrClass[it].key }, true)
                    SpaceV()
                    ShowAbilitiesForClass(klass = klass)
                    SpaceV()
                    ShowButton(text = Res.getLocale("next"), onItemClick = {
                        // Crear el personaje
                        showCharInfo(name)
                    })
                }
            }
        }
    }
    /**
     * Iniciar la otra activity
     */
    private fun showCharInfo(info : String = "")
    {
        var intent = Intent(this, CharActivity::class.java)
        intent.putExtra("char", info)
        startActivity(intent)
        finish()
    }
}

fun configLibrary(assets : AssetManager): Boolean
{
    if(!Res.configResStrings(assets.open(Res.STRINGS_FILE)))
    {
        Log.e(APP_TAG, "Error config " + Res.STRINGS_FILE)
        return false
    }
    if(!Res.configResNames(assets.open(Res.NAMES_FILE)))
    {
        Log.e(APP_TAG, "Error config " + Res.NAMES_FILE)
        return false
    }
    if(!Res.configResFeats(assets.open(Res.FEATS_FILE)))
    {
        Log.e(APP_TAG, "Error config " + Res.FEATS_FILE)
        return false
    }
    return true
}

@Composable
fun SpaceV() = Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dimSpaceV)))

@Composable
fun SpaceH() = Spacer(modifier = Modifier.width(dimensionResource(R.dimen.dimSpaceH)))

@Composable
fun ShowFeat(feat : EFeat)
{
    var data = Res.getLocale(feat)
    var prerequisite : String = data[1]
    var desc : String = data[2]

    if(prerequisite.isNotEmpty())
    {
        Text(text = prerequisite,
            modifier = Modifier.fillMaxWidth(),
            fontSize = dimensionResource(R.dimen.fontText).value.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic
        )
    }
    Text(text = desc,
        modifier = Modifier.fillMaxWidth(),
        fontSize = dimensionResource(R.dimen.fontText).value.sp
    )
}
@Composable
fun ShowAbilitiesForClass(klass : EClass)
{
    val weapons = Res.getValues(Res.getLocale(klass.weaponTypes))
    weapons.addAll(Res.getValues(Res.getLocale(klass.weaponSingles)))
    val tools = when(klass){
        EClass.BARD -> Res.getLocale("bard_tools")
        EClass.MONK -> Res.getLocale("monk_tools")
        else -> Res.asSentence(Res.getLocale(klass.tools), Res.getLocale("no_tools"))
    }
    val skills = when(klass){
        EClass.BARD -> Res.getLocale("bard_skill")
        else -> Res.getLocaleF("sel_skills", klass).lowercase() + " " + Res.asSentence(Res.getLocale(klass.skills))
    }
    var armor = "• " + Res.getLocale("armor") + ": " + Res.asSentence(Res.getLocale(klass.armorTypes), Res.getLocale("no_armor")).lowercase()
    val complement = Res.getLocale(klass, "armor_com")
    if(complement.isNotEmpty())
    {
        armor += ". " + complement
    }
    Text(text = Res.getLocale(klass, "desc"),
        modifier = Modifier.fillMaxWidth(),
        fontSize = dimensionResource(R.dimen.fontText).value.sp,
    )
    Text(text = Res.getLocale("hit_point"),
        modifier = Modifier.fillMaxWidth(),
        fontSize = dimensionResource(R.dimen.fontText).value.sp,
        fontWeight = FontWeight.Bold,
    )
    Text(text = "• " + Res.getLocale("hit_dice") + ": " + Res.getLocaleF("hit_dice", klass),
        modifier = Modifier.fillMaxWidth(),
        fontSize = dimensionResource(R.dimen.fontText).value.sp,
    )
    Text(text = "• " + Res.getLocale("hit_points_level_1") + ": " + Res.getLocaleF("hit_points_level_1", klass),
        modifier = Modifier.fillMaxWidth(),
        fontSize = dimensionResource(R.dimen.fontText).value.sp,
    )
    Text(text = "• " + Res.getLocale("hit_points_other") + ": " + Res.getLocaleF("hit_points_other", klass),
        modifier = Modifier.fillMaxWidth(),
        fontSize = dimensionResource(R.dimen.fontText).value.sp,
    )
    Text(text = Res.getLocale("class_features"),
        modifier = Modifier.fillMaxWidth(),
        fontSize = dimensionResource(R.dimen.fontText).value.sp,
        fontWeight = FontWeight.Bold,
    )
    Text(text = armor,
        modifier = Modifier.fillMaxWidth(),
        fontSize = dimensionResource(R.dimen.fontText).value.sp,
    )
    Text(text = "• " + Res.getLocale("weapons") + ": " + Res.asSentence(weapons).lowercase(),
        modifier = Modifier.fillMaxWidth(),
        fontSize = dimensionResource(R.dimen.fontText).value.sp,
    )
    Text(text = "• " + Res.getLocale("tools") + ": " + tools.lowercase(),
        modifier = Modifier.fillMaxWidth(),
        fontSize = dimensionResource(R.dimen.fontText).value.sp,
    )
    Text(text = "• " + Res.getLocale("saving_throw") + ": " + Res.asSentence(Res.getLocale(klass.savingThrow)).lowercase(),
        modifier = Modifier.fillMaxWidth(),
        fontSize = dimensionResource(R.dimen.fontText).value.sp,
    )
    Text(text = "• " + Res.getLocale("skills") + ": " + skills,
        modifier = Modifier.fillMaxWidth(),
        fontSize = dimensionResource(R.dimen.fontText).value.sp,
    )
}
@Composable
fun ShowAbilitiesForRace(race : ERace)
{
    val racialBonus = ERace.getAbilityBonus(race)
    if(racialBonus.isNotEmpty())
    {
        val arrCols = ArrayList<String>()
        racialBonus.forEach {
            arrCols.add(Res.getLocale(it.key) + " (+" + it.value + ")")
        }
        val columns = Res.columns(arrCols)
        Text(text = Res.getLocale("ability_bonus"),
            modifier = Modifier.fillMaxWidth(),
            fontSize = dimensionResource(R.dimen.fontText).value.sp,
            fontWeight = FontWeight.Bold,
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            columns.forEach {
                Text(text = it,
                    modifier = Modifier
                        .weight(1f, true),
                    fontSize = dimensionResource(R.dimen.fontText).value.sp,
                )
            }
        }
    }
    Text(text = Res.getLocale("racial_abilities"),
        modifier = Modifier.fillMaxWidth(),
        fontSize = dimensionResource(R.dimen.fontText).value.sp,
        fontWeight = FontWeight.Bold,
    )
    Text(text = Res.getLocale("speed") + ": " + race.speed.toString(),
        modifier = Modifier.fillMaxWidth(),
        fontSize = dimensionResource(R.dimen.fontText).value.sp,
    )
    Text(text = Res.getLocale("languages") + ": " + Res.asSentence(Res.getLocale(race.languages)).lowercase(),
        modifier = Modifier.fillMaxWidth(),
        fontSize = dimensionResource(R.dimen.fontText).value.sp,
    )
    val racialAbilities = ERace.getRacialTraits(race)
    if(racialAbilities.isNotEmpty())
    {
        Res.getLocale(racialAbilities).forEach {
            Text(text = "• " + it.value,
                modifier = Modifier.fillMaxWidth(),
                fontSize = dimensionResource(R.dimen.fontText).value.sp,
            )
        }
    }
    /*var arr = ArrayList<String>()
    val racialAbilities = ERace.getRacialAbilities(race)
    arr.add(Res.getLocale("speed") + ": " + race.speed.toString())
    arr.add(Res.getLocale("languages") + ": " + Res.asSentence(Res.getLocale(race.languages)).lowercase())
    if(racialAbilities.isNotEmpty())
    {
        Res.getLocale(racialAbilities).forEach {
            arr.add("• " + it.value)
        }
    }
    Text(text = Res.columns(arr, 1)[0],
        modifier = Modifier.fillMaxWidth(),
        fontSize = dimensionResource(R.dimen.dimTextFont).value.sp,
    )*/
}
@Composable
fun ShowEditText(text: String, onValueChange: (String) -> Unit, enabled: Boolean = true)
{
    var clStroke by remember { mutableStateOf(R.color.clEdNormalStroke) }
    var clText by remember { mutableStateOf(R.color.clEdEnText) }
    var dimStroke by remember { mutableStateOf(R.dimen.dimEdNormalStroke) }

    BasicTextField(
        value = text,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged {
                dimStroke = if (it.hasFocus) R.dimen.dimEdFocusStroke else R.dimen.dimEdNormalStroke
                clText = if (enabled) R.color.clEdEnText else R.color.clEdDisText
                clStroke = if (enabled) {
                    if (it.hasFocus) R.color.clEdFocusStroke else R.color.clEdNormalStroke
                } else {
                    R.color.clEdDisStroke
                }
            }
            .focusable(),
        enabled = enabled,
        textStyle = TextStyle(
            fontSize = dimensionResource(R.dimen.fontText).value.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(clText)
        ),
        cursorBrush = SolidColor(colorResource(R.color.clEdCursor)),
        singleLine = true,
        decorationBox = { innerTextField ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(colorResource(R.color.clEdEnBg), controlShape)
                    .border(dimensionResource(dimStroke), colorResource(clStroke), controlShape)
                    .padding(16.dp, 12.dp)
            ) {
                innerTextField()
            }
        }
    )
    /*OutlinedTextField(
        modifier = Modifier.fillMaxWidth()
            .onFocusChanged {
                if(enabled) {
                    dimStroke = dimEdFocusStroke
                    clStroke = if(it.hasFocus) clEdFocusStroke else clEdNormalStroke
                }
                else {
                    dimStroke = dimNormalStroke
                    clStroke = clEdDisStroke
                }
            }.focusable()
            .background(clEdEnBg, myShape)
            .clip(myShape).border(BorderStroke(dimStroke, clStroke)),
        value = text,
        onValueChange = onValueChange,
        enabled = enabled,
        textStyle = TextStyle(
            fontSize = dimensionResource(R.dimen.dimTextFont).value.sp,
            fontWeight = FontWeight.Bold
        ),
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            textColor = clEdEnText,
            disabledTextColor = clEdDisText,
            containerColor = clEdEnBg,
            cursorColor = clEdCursor,
            errorCursorColor = clRed4,
            /*focusedIndicatorColor = clYellow5,
            unfocusedIndicatorColor = clYellow4,
            disabledIndicatorColor = clEdDisText,
            errorIndicatorColor = clRed3*/
        ),
        shape = myShape
    )*/
    /*TextField(
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged {
                if(enabled)
                {
                    clStroke = if(it.hasFocus) clGreen4 else clYellow4
                }
                else
                {
                    clStroke = clRed4
                }
        }.focusable().clip(myShape).border(BorderStroke(5.dp, clStroke)),
        textStyle = TextStyle(
            fontSize = dimensionResource(R.dimen.dimTextFont).value.sp,
            fontWeight = FontWeight.Bold
        ),
        value = text,
        onValueChange = onValueChange,
        enabled = enabled,
        //isError = true,
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            textColor = clEdEnText,
            disabledTextColor = clEdDisText,
            containerColor = clEdEnBg,
            cursorColor = clEdCursor,
            errorCursorColor = clRed4,
        ),
        shape = myShape
    )*/
}
@Composable
fun <T> ShowDropDown(
    items: ArrayList<Map.Entry<T, String>>,
    initIndex : Int = 0,
    onItemClick: (Int) -> Unit,
    enabled : Boolean = true)
{
    var menuExpanded = remember { mutableStateOf(false) }
    var itemPosition = remember { mutableStateOf(initIndex) }

    Box {
        ShowButton(text = items[itemPosition.value].value, icon = R.drawable.but_list, onItemClick = { menuExpanded.value = true }, enabled = enabled)
        DropdownMenu(
            expanded = menuExpanded.value,
            onDismissRequest = { menuExpanded.value = false}
        ) {
            items.forEachIndexed { index, username ->
                DropdownMenuItem(modifier = Modifier,
                    text = {
                        Text(text = username.value,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = dimensionResource(R.dimen.fontBut).value.sp) },
                    onClick = {
                        menuExpanded.value = false
                        itemPosition.value = index
                        onItemClick(index)
                    })
            }
        }
    }
}
@Composable
fun ShowButton(text: String = "", onItemClick: () -> Unit, enabled : Boolean = true, icon : Int = 0)
{
    val clStroke = colorResource(if(enabled) R.color.clButBrownEnStroke else R.color.clButBrownDisStroke)
    val clTint = colorResource(if(enabled) R.color.clButBrownEnText else R.color.clButBrownDisText)
    val hasText = text.isNotEmpty()
    val hasIcon = (icon > 0)

    Button(
        modifier = Modifier
            .fillMaxWidth()
            .focusable(enabled),
        contentPadding = PaddingValues(8.dp, 0.dp),
        onClick = onItemClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            colorResource(R.color.clButBrownEnBg),
            colorResource(R.color.clButBrownEnText),
            colorResource(R.color.clButBrownDisBg),
            colorResource(R.color.clButBrownDisText)),
        border = BorderStroke(dimensionResource(R.dimen.dimButStroke), clStroke),
        shape = controlShape
    ) {
        if(hasText)
        {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f),
                fontSize = dimensionResource(R.dimen.fontBut).value.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        if(hasText && hasIcon)
        {
            SpaceH()
        }
        if(hasIcon)
        {
            Icon(
                modifier = Modifier.size(ButtonDefaults.IconSize),
                painter = painterResource(icon),
                contentDescription = null,
                tint = clTint
            )
        }
    }
}