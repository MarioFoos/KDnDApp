package com.mlf.kdndapp

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dndlib.DNDChar
import com.dndlib.DNDHitPoints
import com.dndlib.DNDWealth
import com.dndlib.DNDXpLevel
import com.dndlib.base.EAbility
import com.dndlib.base.EAlignment
import com.dndlib.base.EBackground
import com.dndlib.base.EClass
import com.dndlib.base.ECoin
import com.dndlib.base.EEthnicity
import com.dndlib.base.EFeat
import com.dndlib.base.EGender
import com.dndlib.base.ERace
import com.dndlib.base.EStageOfLife
import com.dndlib.res.ComTools
import com.dndlib.res.Res
import com.mlf.kdndapp.ui.theme.KDnDAppTheme


private val coinIcons = arrayOf(R.drawable.coin_platinum, R.drawable.coin_gold,  R.drawable.coin_electrum, R.drawable.coin_silver, R.drawable.coin_cupper)
private val coinTypes = arrayOf(ECoin.PLATINUM, ECoin.GOLD, ECoin.ELECTRUM, ECoin.SILVER, ECoin.CUPPER)

data class DEntry(val key: String = "", val value: String = "", val title: String = "", val desc: String = "")

class CharActivity : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        val race = Res.randomItem(ERace.values())
        val klass = Res.randomItem(EClass.values())
        val gender = Res.randomItem(EGender.values())
        val stageOfLife = Res.randomItem(EStageOfLife.values())
        val ethnicity = Res.randomItem(EEthnicity.values())
        val name = Res.genName(race, ethnicity, gender, stageOfLife) //"Un nombre \"muy largo\" para mostrar en una línea"

        val car = DNDChar(name, race, klass)
        car.gender = gender
        car.ethnicity = ethnicity
        car.genAge(stageOfLife)
        car.setWealth(ComTools.random(80L, 600L))
        car.alignment = Res.randomItem(EAlignment.values())

        car.hp.max = 30
        car.hp.temp = 5
        car.hp.hp = ComTools.random(5, 25)
        car.xp = ComTools.random(30, 270)

        if(race == ERace.VARIANT_HUMAN)
        {
            car.addFeat(EFeat.LUCKY)
        }
        EAbility.values().forEach { ability -> car.setAbilityBase(ability, ComTools.random(10, 20)) }
        car.setAbilityRacialsBonus(EAbility.CHARISMA, EAbility.INTELLIGENCE)
        car.setAbilityExtraBonus(EAbility.STRENGTH, 1)
        car.background = Res.randomItem(EBackground.values())

        setContent {
            var carWealth: MutableState<Long> = rememberSaveable { mutableStateOf(car.wealth.total) }
            var carXp: MutableState<Int> = rememberSaveable { mutableStateOf(car.xp) }

            KDnDAppTheme{
                Box(modifier = Modifier
                    .fillMaxSize()
                    .paint(painterResource(id = R.drawable.bg_hoja_dnd), contentScale = ContentScale.FillBounds)
                ){}
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.mainPadding))
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    // Nombre
                    ShowName(this@CharActivity, car.name)
                    SpaceV()
                    // Oro
                    ShowWealth(context = this@CharActivity, wealth = carWealth, onChange = { wealth ->
                        car.wealth.total = wealth
                        carWealth.value = car.wealth.total
                    })
                    SpaceV()
                    Row(verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth())
                    {
                        Column(modifier = Modifier.width(dimensionResource(R.dimen.profile)))
                        {
                            // Perfil
                            ShowProfile(context = this@CharActivity, car = car,
                                size = dimensionResource(R.dimen.profile),
                                xp = carXp,
                                // Nivel
                                onChangeLevel = { newData ->
                                    car.xpLevel.setXp(newData)
                                    carXp.value = car.xpLevel.xp
                                },
                                // Inspiración
                                onChangeInspiration =  { newInspiration ->
                                    car.inspiration = newInspiration
                                })
                            SpaceV()
                            // HP
                            Row(modifier = Modifier.fillMaxWidth())
                            {
                                ShowHP(context = this@CharActivity, car = car,
                                    onChangeData = { newHp -> car.hp.set(newHp) })
                            }
                            SpaceV()
                            // XP
                            Row(modifier = Modifier.fillMaxWidth())
                            {
                                ShowXP(context = this@CharActivity, car = car,
                                    xp = carXp,
                                    onChangeData = { newData ->
                                        car.xpLevel.setXp(newData)
                                        carXp.value = car.xpLevel.xp
                                    })
                            }
                        }
                        SpaceH()
                        ShowBasics(this@CharActivity, car)
                    }
                    SpaceV()
                    ShowRacials(this@CharActivity, car)
                    SpaceV()
                    ShowLanguages(this@CharActivity, car)
                    SpaceV()
                    ShowFeats(this@CharActivity, car)
                    SpaceV()
                    ShowPrimaryAbility(this@CharActivity, car)
                    SpaceV()
                    ShowSavingThrow(this@CharActivity, car)
                    SpaceV()
                    ShowHitDice(this@CharActivity, car)
                    SpaceV()
                }
            }
        }
    }
}

/******************************************************************************************************************
 *                                               GET IMAGES
 *******************************************************************************************************************/

fun getImageGender(context: Context, gender : EGender) : ImageBitmap
{
    var file = "imgs/gender/" + gender.name.lowercase() + ".png"
    var img = BitmapFactory.decodeStream(context.assets.open(file))
    return img.asImageBitmap()
}
fun getImageKlass(context: Context, klass : EClass) : ImageBitmap
{
    var file = "imgs/klass/" + klass.name.lowercase() + ".png"
    var img = BitmapFactory.decodeStream(context.assets.open(file))
    return img.asImageBitmap()
}
fun getImageRace(context: Context, race : ERace, gender : EGender) : ImageBitmap
{
    var file = "imgs/race/"

    file += when(race)
    {
        ERace.DWARF, ERace.HILL_DWARF, ERace.MOUNTAIN_DWARF, ERace.DUERGAR -> "dwarf_"
        ERace.ELF, ERace.HIGH_ELF, ERace.WOOD_ELF, ERace.DARK_ELF -> "elf_"
        ERace.HALFLING, ERace.LIGHTFOOT, ERace.STOUT -> "halfling_"
        ERace.HUMAN, ERace.VARIANT_HUMAN -> "human_"
        ERace.DRAGONBORN -> "dragonborn_"
        ERace.GNOME, ERace.FOREST_GNOME, ERace.ROCK_GNOME -> "gnome_"
        ERace.HALFELF -> "halfelf_"
        ERace.HALFORC -> "halforc_"
        ERace.TIEFLING -> "tiefling_"
        else -> "human_"
    }
    file += if(gender == EGender.MALE){ "m"} else { "f" }
    file += ".png"

    var img = BitmapFactory.decodeStream(context.assets.open(file))
    return img.asImageBitmap()
}

/******************************************************************************************************************
*                                               COMPOSABLES
*******************************************************************************************************************/

@Composable
fun ShowAbilities(context: Context, car: DNDChar)
{
    Row(modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically)
    {


    }
}

@Composable
fun ShowHitDice(context: Context, car: DNDChar)
{
    Row(modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically)
    {
        var items = ArrayList<DEntry>()
        items.add(DEntry(value = Res.localeF("hit_dice", car.klass)))
        ShowItemsList(context = context, name = Res.locale("hit_dice") + ": ", list = items)
    }
}
@Composable
fun ShowSavingThrow(context: Context, car: DNDChar)
{
    Row(modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically)
    {
        var items = ArrayList<DEntry>()
        car.savingThrow.forEach {  item ->
            val name = Res.locale(item)
            items.add(DEntry(value = name, title = name, desc = Res.locale(item, "desc")))
        }
        ShowItemsList(context = context, name = Res.locale("saving_throw") + ": ", list = items)
    }
}
@Composable
fun ShowPrimaryAbility(context: Context, car: DNDChar)
{
    Row(modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically)
    {
        var items = ArrayList<DEntry>()
        car.primaryAbility.forEach {  item ->
            val name = Res.locale(item)
            items.add(DEntry(value = name, title = name, desc = Res.locale(item, "desc")))
        }
        ShowItemsList(context = context, name = Res.locale("primary_abilily") + ": ", list = items)
    }
}
@Composable
fun ShowFeats(context: Context, car: DNDChar)
{
    Row(modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically)
    {
        var items = ArrayList<DEntry>()
        car.feats.forEach {  item ->
            items.add(DEntry(value = Res.locale(item), title = Res.locale(item), desc = Res.locale(item, "desc")))
        }
        ShowItemsList(context = context, name = Res.locale("feats") + ": ", list = items)
    }
}
@Composable
fun ShowLanguages(context: Context, car: DNDChar)
{
    Row(modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically)
    {
        var items = ArrayList<DEntry>()
        car.languages.forEach { item ->
            items.add(DEntry(value = Res.locale(item), title = Res.locale(item), desc = Res.locale(item, "desc")))
        }
        ShowItemsList(context = context, name = Res.locale("languages") + ": ", list = items)
    }
}
@Composable
fun ShowRacials(context: Context, car: DNDChar)
{
    Row(modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically)
    {
        var items = ArrayList<DEntry>()
        car.racialTraits.forEach {  item ->
            items.add(DEntry(value = Res.locale(item), title = Res.locale(item), desc = Res.locale(car.race, item)))
        }
        ShowItemsList(context = context, name = Res.locale("racials") + ": ", list = items)
    }
}
@Composable
fun ShowClickeable(context: Context, entry: DEntry)
{
    if(entry.title.isEmpty())
    {
        Box(modifier = Modifier
            .background(colorResource(R.color.clNone))
            .padding(dimensionResource(R.dimen.contentPadding), 0.dp)
        )
        {
            Text(text = entry.value,
                fontSize = fontSizeResourse(R.dimen.fontNormal),
                color = colorResource(R.color.textNormalFace))
        }
    }
    else
    {
        Box(modifier = Modifier
            .background(colorResource(R.color.clickeableBg))
            .padding(dimensionResource(R.dimen.contentPadding), 0.dp)
        )
        {
            var posX by rememberSaveable { mutableStateOf(0f) }
            var posY by rememberSaveable { mutableStateOf(0f) }

            Text(text = entry.value,
                fontSize = fontSizeResourse(R.dimen.fontNormal),
                textDecoration = TextDecoration.Underline,
                color = colorResource(R.color.textNormalFace),
                modifier = Modifier
                    .onGloballyPositioned { coordinates ->
                        posX = coordinates.positionInWindow().x
                        posY = coordinates.positionInWindow().y
                    }
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { offset ->
                                dialogInfo(
                                    context = context, x = posX + offset.x, y = posY + offset.y,
                                    title = entry.title, desc = entry.desc
                                )
                            }
                        )
                    }
            )
        }
    }
}
@Composable
fun ShowItemsList(context: Context, name: String = "", list: ArrayList<DEntry>)
{
    if(list.isEmpty())
    {
        list.add(DEntry(value = "—"))
    }
    Row(modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically)
    {
        // Etiqueta de la lista
        if(name.isNotEmpty())
        {
            Text(text = name,
                fontSize = fontSizeResourse(R.dimen.fontNormal),
                fontWeight = FontWeight.Bold
            )
        }
        // Lista
        Row(modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically)
        {
            list.forEach { entry ->
                ShowClickeable(context = context, entry = entry)
                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.controlMarginH)))
            }
        }
    }
}
@Composable
fun ShowBasics(context: Context, car: DNDChar)
{
    var arrNames : ArrayList<String> = ArrayList()
    var arrData : ArrayList<DEntry> = ArrayList()

    // Altura
    arrNames.add(Res.locale("height") + ": ")
    arrData.add(DEntry(value = car.height.toString()))
    // Peso
    arrNames.add(Res.locale("weight") + ": ")
    arrData.add(DEntry(value = car.weight.toString()))
    // Edad
    arrNames.add(Res.locale("age") + ": ")
    arrData.add(DEntry(value = car.age.toString() + " " + Res.locale("years")))
    // Velocidad
    arrNames.add(Res.locale("speed") + ": ")
    arrData.add(DEntry(value = car.speed.toString()))
    // Alineamiento
    arrNames.add(Res.locale("alignment") + ": ")
    arrData.add(DEntry(value = Res.locale(car.alignment), title = Res.locale(car.alignment), desc = Res.locale(car.alignment, "desc")))
    // Trasfondo
    arrNames.add(Res.locale("backgroud") + ": ")
    arrData.add(DEntry(value = Res.locale(car.background), title = Res.locale(car.background), desc = Res.locale(car.background, "desc")))

    Column(modifier = Modifier.fillMaxWidth())
    {
        Row(modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically)
        {
            // Raza y clase
            Text(modifier = Modifier.fillMaxWidth(),
                text = Res.locale(car.klass) + " " + Res.locale(car.race),
                textAlign = TextAlign.Center,
                fontSize = fontSizeResourse(R.dimen.fontNormal),
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.controlMarginV)))
        arrNames.forEachIndexed { index, item ->
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically)
            {
                Text(text = item,
                    fontSize = fontSizeResourse(R.dimen.fontNormal),
                    fontWeight = FontWeight.Bold
                )
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                    verticalAlignment = Alignment.CenterVertically)
                {
                    ShowClickeable(context = context, entry = arrData[index])
                }
            }
            if(index < arrNames.size - 1)
            {
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.controlMarginV)))
            }
        }
    }
}
@Composable
fun ShowBar(context: Context, modifier: Modifier = Modifier,
            color: Color,
            text: String? = null,
            fraction: Float = 0f)
{
    var boxWidth by rememberSaveable { mutableStateOf(0) }

    Box(modifier = modifier
        .fillMaxWidth()
        .onSizeChanged { size -> boxWidth = size.width }
        .background(colorResource(R.color.contentBg))
        .padding(dimensionResource(R.dimen.contentStroke))
        .border(width = dimensionResource(R.dimen.contentStroke), color = colorResource(R.color.contentFace)),
    )
    {
        if(boxWidth > 0)
        {
            Box(modifier = Modifier
                .fillMaxHeight()
                .width(pxToDp(boxWidth.toFloat() * fraction))
                .align(alignment = Alignment.CenterStart)
                .background(color)
            ){}
            if(text != null)
            {
                Box(modifier = Modifier.align(Alignment.Center))
                {
                    Text(text = text,
                        fontSize = fontSizeResourse(R.dimen.fontMini),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.clWhite)
                    )
                }
            }
        }
    }
}
@Composable
fun ShowBar(context: Context, modifier: Modifier = Modifier,
            color: Color,
            text: String? = null,
            max: Int = 100,
            value: Int = 0)
{
    ShowBar(context = context, modifier = modifier,
        color = color,
        text = text,
        fraction = if(max != 0){ value.toFloat()/max.toFloat()} else{ 0f })

}
@Composable
fun ShowHP(context: Context, car: DNDChar,
           onChangeData: (DNDHitPoints)->Unit = { _ -> })
{
    var gPosX by rememberSaveable { mutableStateOf(0f) }
    var gPosY by rememberSaveable { mutableStateOf(0f) }
    var showHp by rememberSaveable { mutableStateOf(car.hp.hp) }
    var showMax by rememberSaveable { mutableStateOf(car.hp.max) }
    var showTemp by rememberSaveable { mutableStateOf(car.hp.temp) }
    var hpShow = DNDHitPoints(showHp, showMax, showTemp)

    val desc =  "• " + Res.locale("hit_points_level_1") + ": " + Res.localeF("hit_points_level_1", car.klass) + "\n" +
                "• " + Res.locale("hit_points_other") + ": " + Res.localeF("hit_points_other", car.klass)

    ShowBar(context = context,
        color = colorResource(id = R.color.barHP),
        fraction = hpShow.fraction,
        text = car.hp.toString(),
        modifier = Modifier
            .height(dimensionResource(R.dimen.profileBarH))
            .onGloballyPositioned { coordinates ->
                gPosX = coordinates.positionInWindow().x
                gPosY = coordinates.positionInWindow().y
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { offset ->
                        dialogInfo(
                            context = context, x = gPosX + offset.x, y = gPosY + offset.y,
                            title = Res.locale("hit_points") + " (" + hpShow.toString() + ")",
                            desc = desc,
                        )
                    },
                    onLongPress = { offset ->
                        dialogHp(context = context, x = gPosX + offset.x, y = gPosY + offset.y,
                            hp = car.hp,
                            onAccept = { hp ->
                                showHp = hp.hp
                                showMax = hp.max
                                showTemp = hp.temp
                                onChangeData(hp)
                            })
                    }
                )
            }
    )
}
@Composable
fun ShowXP(context: Context, car: DNDChar,
           xp: MutableState<Int>,
           onChangeData: (DNDXpLevel)->Unit = { _ -> })
{
    var gPosX by rememberSaveable { mutableStateOf(0f) }
    var gPosY by rememberSaveable { mutableStateOf(0f) }

    var levelXp = DNDXpLevel(xp.value)

    ShowBar(context = context,
        color = colorResource(id = R.color.barXP),
        text = levelXp.toString(),
        fraction = levelXp.xpFractionInLevel,
        modifier = Modifier
            .height(dimensionResource(R.dimen.profileBarH))
            .onGloballyPositioned { coordinates ->
                gPosX = coordinates.positionInWindow().x
                gPosY = coordinates.positionInWindow().y
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { offset ->
                        dialogInfo(
                            context = context, x = gPosX + offset.x, y = gPosY + offset.y,
                            title = Res.locale("xp"),
                            desc = levelXp.toString()
                        )
                    },
                    onLongPress = { offset ->
                        dialogXp(context = context, x = gPosX + offset.x, y = gPosY + offset.y,
                            onAccept = { addXp ->
                                levelXp.addXp(addXp)
                                onChangeData(levelXp)
                            })
                    }
                )
            }
    )
}
@Composable
fun ShowProfile(context: Context, size: Dp,
                car: DNDChar,
                xp: MutableState<Int>,
                onChangeLevel: (DNDXpLevel)->Unit = { _ -> },
                onChangeInspiration: (Int)->Unit = { _ -> })
{
    var showInspiration by rememberSaveable { mutableStateOf(car.inspiration) }
    var xpLevel = DNDXpLevel(xp.value)

    Box(modifier = Modifier.size(size))
    {
        val iconKlass = size*0.33f
        val iconSize = size*0.27f

        // Avatar
        ShowProfileContent(bitmap = getImageRace(context, race = car.race, gender = car.gender),
            size = size, tint = false,
            modifier = Modifier.align(alignment = Alignment.Center))
        // Nivel
        ShowProfileContent(number = xpLevel.level, size = iconSize,
            modifier = Modifier.align(alignment = Alignment.TopStart),
            onTap = { x, y ->
                dialogInfo(context = context, x = x, y = y,
                    title = Res.locale("level"),
                    desc = Res.locale("level_desc"))
            },
            onLongPress = { x, y ->
                dialogEditNum(context = context, x = x, y = y,
                    title = Res.locale("level"),
                    value = xpLevel.level,
                    onAccept = { newLevel ->
                        xpLevel.level = newLevel
                        onChangeLevel(xpLevel)
                    })
            })
        // Inspitación
        ShowProfileContent(number = showInspiration, size = iconSize,
            modifier = Modifier.align(alignment = Alignment.TopEnd),
            onTap = { x, y ->
                dialogInfo(context = context, x = x, y = y,
                    title = Res.locale("inspiration"),
                    desc = Res.locale("inspiration_desc"))
            },
            onLongPress = { x, y ->
                dialogEditNum(context = context, x = x, y = y,
                    title = Res.locale("inspiration"),
                    value = showInspiration,
                    onAccept = { newInspiration ->
                        showInspiration = newInspiration
                        onChangeInspiration(newInspiration)
                    })
            })
        // Clase
        ShowProfileContent(bitmap = getImageKlass(context, car.klass), size = iconKlass,
            modifier = Modifier.align(alignment = Alignment.BottomEnd),
            onTap = { x, y ->
                dialogInfo(context = context, x = x, y = y,
                    title = Res.locale(car.klass),
                    desc = Res.locale(car.klass, "desc"))
            })
        // Género
        ShowProfileContent(bitmap = getImageGender(context, car.gender), size = iconSize,
            modifier = Modifier.align(alignment = Alignment.BottomStart),
            onTap = { x, y ->
                dialogInfo(context = context, x = x, y = y,
                    title = Res.locale("gender"))
            })
    }
}
@Composable
fun ShowWealth(context: Context, wealth: MutableState<Long>, onChange: (newWealth: Long)->Unit = { _ -> })
{
    val dndWealth = DNDWealth(wealth.value)

    Row(verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(R.color.wealthBg))
            .border(dimensionResource(R.dimen.contentStroke), colorResource(R.color.contentStroke))
            .padding(dimensionResource(R.dimen.contentPadding))
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { _ ->
                        dialogWealth(context = context,
                            wealth = wealth.value,
                            onAccept = { newWealth -> onChange(newWealth) })
                    }
                )
            }
    )
    {
        coinTypes.forEachIndexed { index, type ->
            Text(
                text = dndWealth.get(type).toString(),
                fontSize = fontSizeResourse(R.dimen.fontWealth),
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.clWhite)
            )
            Image(
                modifier = Modifier
                    .height(dimensionResource(R.dimen.fontWealth).value.dp)
                    .padding(dimensionResource(R.dimen.coinPadding), 0.dp),
                painter = painterResource(id = coinIcons[index]),
                contentDescription = null
            )
            if(index < coinTypes.size - 1)
            {
                SpaceH()
            }
        }
    }
}
@Composable
fun ShowName(context: Context, name: String)
{
    Row(verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(R.color.contentBg))
            .border(dimensionResource(R.dimen.contentStroke), colorResource(R.color.contentStroke))
            .padding(dimensionResource(R.dimen.contentPadding))
    ){
        // Nombre
        Text(text = name,
            modifier = Modifier.fillMaxWidth(),
            fontSize = fontSizeResourse(R.dimen.fontName),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = colorResource(R.color.contentFace)
        )
    }
}
@Composable
fun ShowProfileContent(modifier: Modifier = Modifier, size : Dp,
                       bitmap: ImageBitmap? = null,
                       number: Int? = null,
                       tint : Boolean = true,
                       onTap: (x: Float, y: Float)->Unit = { _, _ -> },
                       onLongPress: (x: Float, y: Float)->Unit = { _, _ -> })
{
    var gPosX by rememberSaveable { mutableStateOf(0f) }
    var gPosY by rememberSaveable { mutableStateOf(0f) }

    Box(contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(colorResource(R.color.contentBg))
            .padding(dimensionResource(R.dimen.contentStroke))
            .border(shape = CircleShape, width = dimensionResource(R.dimen.contentStroke), color = colorResource(R.color.contentFace))
            .padding(dimensionResource(R.dimen.contentStroke))
            .onGloballyPositioned { coordinates ->
                gPosX = coordinates.positionInWindow().x
                gPosY = coordinates.positionInWindow().y
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { offset -> onTap(gPosX + offset.x, gPosY + offset.y) },
                    onLongPress = { offset -> onLongPress(gPosX + offset.x, gPosY + offset.y) }
                )
            }
    )
    {
        if(number != null)
        {
            Text(
                text = number.toString(),
                textAlign = TextAlign.Center,
                fontSize = fontSizeResourse(R.dimen.fontProfile),
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = colorResource(R.color.contentFace)
            )
        }
        else if(bitmap != null)
        {
            Image(modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape),
                contentDescription = null,
                bitmap = bitmap,
                contentScale = ContentScale.Fit,
                colorFilter = if(tint){ ColorFilter.tint(colorResource(R.color.contentFace), BlendMode.SrcAtop) } else { null }
            )
        }
    }
}
