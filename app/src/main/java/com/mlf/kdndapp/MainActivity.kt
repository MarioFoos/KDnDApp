package com.mlf.kdndapp

import android.content.res.AssetManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dndlib.base.EAbility
import com.dndlib.base.EAlignment
import com.dndlib.base.EClass
import com.dndlib.base.EFeat
import com.dndlib.base.EGender
import com.dndlib.base.EInfo
import com.dndlib.base.ERace
import com.dndlib.base.EStageOfLife
import com.dndlib.res.Res
import com.mlf.kdndapp.ui.theme.KDnDAppTheme
import java.util.Locale
import kotlin.system.exitProcess

val APP_TAG = "AppTag"

class MainActivity : ComponentActivity()
{
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        // Configurar librería
        if(!configLibrary(assets))
        {
            exitProcess(-1)
        }
        // Arrays
        val arrRace : ArrayList<Map.Entry<ERace, String>> = Res.getLocale(ERace.values())
        val arrClass : ArrayList<Map.Entry<EClass, String>> = Res.getLocale(EClass.values())
        val arrAlign : ArrayList<Map.Entry<EAlignment, String>> = Res.getLocale(EAlignment.values())
        val arrGender : ArrayList<Map.Entry<EGender, String>> = Res.getLocale(EGender.values())
        val arrStage : ArrayList<Map.Entry<EStageOfLife, String>> = Res.getLocale(EStageOfLife.values())
        val arrFeat : ArrayList<Map.Entry<EFeat, String>> = Res.getLocale(EFeat.values())
        val arrAbility : ArrayList<Map.Entry<EAbility, String>> = Res.getLocale(EAbility.values())

        // Valores iniciales
        val raceInit : Int = Res.randomIndex(arrRace)
        val classInit : Int = Res.randomIndex(arrClass)
        val alignInit : Int = Res.randomIndex(arrAlign)
        val genderInit : Int  = Res.randomIndex(arrGender)
        val stageInit : Int = Res.randomIndex(arrStage)
        val featInit : Int = Res.randomIndex(arrFeat)
        val ability1Init : Int = Res.randomIndex(arrAbility)
        val ability2Init : Int = Res.randomIndex(arrAbility)
        setContent {
            KDnDAppTheme {
                var race by rememberSaveable { mutableStateOf(arrRace[raceInit].key) }
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
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Altura
                        Column(Modifier.weight(1f, true)) {
                            ShowButton(text = height.toString() + " m", icon = R.drawable.d20, onItemClick = { height = ERace.genHeight(race) })
                        }
                        SpaceH()
                        // Peso
                        Column(Modifier.weight(1f, true)) {
                            ShowButton(text = weight.toString() + " kg", icon = R.drawable.d20, onItemClick = { weight = ERace.genWeight(race) })
                        }
                    }
                    SpaceV()
                    ShowAbilitiesForRace(race = race)
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
                    ShowDropDown(arrClass, initIndex = classInit, onItemClick = { klass = arrClass[it].key }, true)
                    ShowAbilitiesForClass(klass = klass)
                    ShowDropDown(arrAlign, initIndex = alignInit, onItemClick = { align = arrAlign[it].key }, true)
                }
            }
        }
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
        Log.e(APP_TAG, "Error config " + Res.FEATS_FILE);
        return false
    }
    if(!Res.configResClasses(assets.open(Res.CLASSES_FILE)))
    {
        Log.e(APP_TAG, "Error config " + Res.CLASSES_FILE);
        return false
    }
    return true
}

@Composable
fun SpaceV() = Spacer(modifier = Modifier.height(4.dp))

@Composable
fun SpaceH() = Spacer(modifier = Modifier.width(8.dp))

@Composable
fun ShowFeat(feat : EFeat)
{
    var data = Res.getLocale(feat)
    var prerequisite : String = data[1]
    var desc : String = data[2]

    if(prerequisite.isNotEmpty())
    {
        Text(text = prerequisite,
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic
        )
    }
    Text(text = desc,
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp),
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
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

    Text(text = Res.getLocaleInfo(klass, EInfo.DESC),
        modifier = Modifier.fillMaxWidth(),
        fontSize = 18.sp,
    )
    Text(text = Res.getLocale("hit_point"),
        modifier = Modifier.fillMaxWidth(),
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
    )
    Text(text = "• " + Res.getLocale("hit_dice") + ": " + Res.getLocaleF("hit_dice", klass),
        modifier = Modifier.fillMaxWidth(),
        fontSize = 18.sp,
    )
    Text(text = "• " + Res.getLocale("hit_points_level_1") + ": " + Res.getLocaleF("hit_points_level_1", klass),
        modifier = Modifier.fillMaxWidth(),
        fontSize = 18.sp,
    )
    Text(text = "• " + Res.getLocale("hit_points_other") + ": " + Res.getLocaleF("hit_points_other", klass),
        modifier = Modifier.fillMaxWidth(),
        fontSize = 18.sp,
    )
    Text(text = Res.getLocale("class_features"),
        modifier = Modifier.fillMaxWidth(),
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
    )
    Text(text = "• " + Res.getLocale("armor") + ": " + Res.asSentence(Res.getLocale(klass.armorTypes), Res.getLocale("no_armor")).lowercase(),
        modifier = Modifier.fillMaxWidth(),
        fontSize = 18.sp,
    )
    Text(text = "• " + Res.getLocale("weapons") + ": " + Res.asSentence(weapons).lowercase(),
        modifier = Modifier.fillMaxWidth(),
        fontSize = 18.sp,
    )
    Text(text = "• " + Res.getLocale("tools") + ": " + tools.lowercase(),
        modifier = Modifier.fillMaxWidth(),
        fontSize = 18.sp,
    )
    Text(text = "• " + Res.getLocale("saving_throw") + ": " + Res.asSentence(Res.getLocale(klass.savingThrow)).lowercase(),
        modifier = Modifier.fillMaxWidth(),
        fontSize = 18.sp,
    )
    Text(text = "• " + Res.getLocale("skills") + ": " + skills,
        modifier = Modifier.fillMaxWidth(),
        fontSize = 18.sp,
    )
    SpaceV()

    /*val racialAbilities = ERace.getRacialAbilities(race)
    if(racialAbilities.isNotEmpty())
    {
        Res.getLocale(racialAbilities).forEach {
            Text(text = "• " + it.value,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 18.sp,
                overflow = TextOverflow.Ellipsis
            )
        }
    }*/
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
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            columns.forEach {
                Text(text = it,
                    modifier = Modifier
                        .weight(1f, true),
                    fontSize = 18.sp,
                )
            }
        }
        SpaceV()
    }
    Text(text = Res.getLocale("racial_abilities"),
        modifier = Modifier.fillMaxWidth(),
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
    )
    Text(text = Res.getLocale("speed") + ": " + race.speed.toString(),
        modifier = Modifier.fillMaxWidth(),
        fontSize = 18.sp,
    )
    Text(text = Res.getLocale("languages") + ": " + Res.asSentence(Res.getLocale(race.languages)).lowercase(),
        modifier = Modifier.fillMaxWidth(),
        fontSize = 18.sp,
    )
    val racialAbilities = ERace.getRacialAbilities(race)
    if(racialAbilities.isNotEmpty())
    {
        Res.getLocale(racialAbilities).forEach {
            Text(text = "• " + it.value,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 18.sp,
            )
        }
    }
    SpaceV()
}
@Composable
fun ShowButton(text: String, onItemClick: () -> Unit, enabled : Boolean = true, icon : Int = 0)
{
    val strokeColor = if(enabled)  colorResource(id = R.color.but_brown_enabled_border) else colorResource(id = R.color.yellow_dark)

    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp),
        contentPadding = PaddingValues(8.dp, 0.dp),
        onClick = onItemClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            colorResource(id = R.color.but_brown_enabled_bg),
            colorResource(id = R.color.but_brown_enabled_text),
            colorResource(id = R.color.but_brown_dis_bg),
            colorResource(id = R.color.but_brown_dis_text),
        ),
        border = BorderStroke(dimensionResource(id = R.dimen.but_stroke), strokeColor),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        if(icon > 0)
        {
            SpaceH()
            Icon(
                modifier = Modifier.size(ButtonDefaults.IconSize),
                painter = painterResource(icon),
                contentDescription = null,
                tint = colorResource(id = R.color.but_brown_enabled_text)
            )
        }
    }
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
                        modifier = Modifier
                            .padding(2.dp)
                            .fillMaxWidth(),
                        fontSize = 20.sp) },
                    onClick = {
                        menuExpanded.value = false
                        itemPosition.value = index
                        onItemClick(index)
                    })
            }
        }
    }
}
