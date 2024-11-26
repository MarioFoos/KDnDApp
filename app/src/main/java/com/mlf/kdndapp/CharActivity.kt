package com.mlf.kdndapp

import android.app.Dialog
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
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
import androidx.compose.ui.unit.sp
import com.dndlib.DNDChar
import com.dndlib.DNDWealth
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

//        val sp_px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 18f, resources.displayMetrics);
//        Log.e(APP_TAG, "PX: " + sp_px)
//        val dp_px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18f, resources.displayMetrics);
//        Log.e(APP_TAG, "PX: " + dp_px)
//        Log.e(APP_TAG, "density: " + resources.displayMetrics.density)

        val race = Res.randomItem(ERace.values())
        val klass = Res.randomItem(EClass.values())
        val gender = Res.randomItem(EGender.values())
        val stageOfLife = Res.randomItem(EStageOfLife.values())
        val ethnicity = Res.randomItem(EEthnicity.values())
        val name = Res.genName(race, ethnicity, gender, stageOfLife)

        val car = DNDChar(name, race, klass)
        car.genAge(stageOfLife)
        car.setWealth(ComTools.random(30L, 500L))
        car.alignment = Res.randomItem(EAlignment.values())
        car.addFeat(EFeat.LUCKY)
        EAbility.values().forEach { ability -> car.setAbilityBase(ability, ComTools.random(10, 20)) }
        car.setAbilityRacialsBonus(EAbility.CHARISMA, EAbility.INTELLIGENCE)
        car.setAbilityExtraBonus(EAbility.STRENGTH, 1)
        car.background = Res.randomItem(EBackground.values())

        setContent {
            var carWealth by rememberSaveable { mutableStateOf(car.wealth.total) }

            KDnDAppTheme{
                Box(modifier = Modifier
                    .fillMaxSize()
                    .paint(painterResource(id = R.drawable.bg_hoja_dnd), contentScale = ContentScale.FillBounds)
                ){}
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.dimMainPadding))
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    // Oro
                    ShowName(this@CharActivity, car)
                    SpaceV()
                    ShowWealth(context = this@CharActivity, wealth = carWealth, onChange = { wealth ->
                        carWealth = wealth
                        car.wealth.total = wealth
                    })
                    SpaceV()
                    // Pefil y Básicos
                    Row(verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier
                            .height(dimensionResource(R.dimen.dimProfile))
                            .fillMaxWidth())
                    {
                        // Perfil
                        Box(contentAlignment = Alignment.Center,
                            modifier = Modifier.size(dimensionResource(R.dimen.dimProfile))
                        )
                        {
                            ShowRoundContent(bitmap = getImageRace(race = car.race, gender = car.gender),
                                size = dimensionResource(R.dimen.dimProfile), tint = false)

                            ShowRoundContent(bitmap = getImageKlass(car.klass), size = dimensionResource(R.dimen.dimKlass),
                                modifier = Modifier.align(alignment = Alignment.BottomEnd),
                                onClick = { x, y ->
                                    dialogInfo(context = this@CharActivity,
                                        title = Res.getLocale(car.klass),
                                        desc = Res.getLocale(car.klass, "desc"),
                                        x = x,
                                        y = y)
                                })
                            ShowRoundContent(bitmap = getImageGender(car.gender), size = dimensionResource(R.dimen.dimGender),
                                modifier = Modifier.align(alignment = Alignment.BottomStart),
                                onClick = { x, y ->
                                    dialogInfo(context = this@CharActivity,
                                        title = Res.getLocale("gender"),
                                        x = x, y = y)
                                })
                            ShowRoundContent(number = car.level, size = dimensionResource(R.dimen.dimLevel),
                                modifier = Modifier.align(alignment = Alignment.TopStart),
                                onClick = { x, y ->
                                    dialogInfo(context = this@CharActivity,
                                        title = Res.getLocale("level"),
                                        desc = Res.getLocale("level_desc"),
                                        x = x,
                                        y = y)
                                })
                            ShowRoundContent(number = car.inspiration, size = dimensionResource(R.dimen.dimLevel),
                                modifier = Modifier.align(alignment = Alignment.TopEnd),
                                onClick = { x, y ->
                                    dialogInfo(context = this@CharActivity,
                                        title = Res.getLocale("inspiration"),
                                        desc = Res.getLocale("inspiration_desc"),
                                        x = x,
                                        y = y)
                                })
                        }
                        SpaceH()
                        Column(modifier = Modifier.fillMaxWidth())
                        {
                            ShowBasics(this@CharActivity, car)
                        }
                    }
                    SpaceV()
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth())
                    {
                        ShowRacials(this@CharActivity, car)
                    }
                }
            }
        }
    }
    private fun getImageGender(gender : EGender) : ImageBitmap
    {
        var file = "imgs/gender/" + gender.name.lowercase() + ".png"
        var img = BitmapFactory.decodeStream(assets.open(file))
        return img.asImageBitmap()
    }
    private fun getImageKlass(klass : EClass) : ImageBitmap
    {
        var file = "imgs/klass/" + klass.name.lowercase() + ".png"
        var img = BitmapFactory.decodeStream(assets.open(file))
        return img.asImageBitmap()
    }
    private fun getImageRace(race : ERace, gender : EGender) : ImageBitmap
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

        var img = BitmapFactory.decodeStream(assets.open(file))
        return img.asImageBitmap()
    }
}
private fun dialogWealth(context: Context, wealth: Long, onAccept: (newWealth: Long)->Unit = { _ -> })
{
    var curWealth = DNDWealth(wealth)

    val dialog = Dialog(context)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(true)
    dialog.setContentView(R.layout.wealth_layout)

    val butOk = dialog.findViewById(R.id.butOk)  as Button
    val butCancel = dialog.findViewById(R.id.butCancel)  as Button

    val textPlatinum = dialog.findViewById(R.id.textPlatinum) as TextView
    val textGold = dialog.findViewById(R.id.textGold) as TextView
    val textElectrum = dialog.findViewById(R.id.textElectrum) as TextView
    val textSilver = dialog.findViewById(R.id.textSilver) as TextView
    val textCupper = dialog.findViewById(R.id.textCupper) as TextView

    val butPlatinumInc = dialog.findViewById(R.id.butPlatinumInc)  as Button
    val butPlatinumDec = dialog.findViewById(R.id.butPlatinumDec)  as Button
    val butGoldInc = dialog.findViewById(R.id.butGoldInc)  as Button
    val butGoldDec = dialog.findViewById(R.id.butGoldDec)  as Button
    val butElectrumInc = dialog.findViewById(R.id.butElectrumInc)  as Button
    val butElectrumDec = dialog.findViewById(R.id.butElectrumDec)  as Button
    val butSilverInc = dialog.findViewById(R.id.butSilverInc)  as Button
    val butSilverDec = dialog.findViewById(R.id.butSilverDec)  as Button
    val butCupperInc = dialog.findViewById(R.id.butCupperInc)  as Button
    val butCupperDec = dialog.findViewById(R.id.butCupperDec)  as Button

    fun showData()
    {
        var platinum = curWealth.get(ECoin.PLATINUM)
        var gold = curWealth.get(ECoin.GOLD)
        var electrum = curWealth.get(ECoin.ELECTRUM)
        var silver = curWealth.get(ECoin.SILVER)
        var cupper = curWealth.get(ECoin.CUPPER)

        textPlatinum.text = platinum.toString()
        textGold.text = gold.toString()
        textElectrum.text = electrum.toString()
        textSilver.text = silver.toString()
        textCupper.text = cupper.toString()
    }

    butPlatinumInc.setOnClickListener {
        curWealth.add(ECoin.PLATINUM, 1);
        showData()
    }
    butPlatinumDec.setOnClickListener {
        curWealth.substract(ECoin.PLATINUM, 1);
        showData()
    }
    butGoldInc.setOnClickListener {
        curWealth.add(ECoin.GOLD, 1);
        showData()
    }
    butGoldDec.setOnClickListener {
        curWealth.substract(ECoin.GOLD, 1);
        showData()
    }
    butElectrumInc.setOnClickListener {
        curWealth.add(ECoin.ELECTRUM, 1);
        showData()
    }
    butElectrumDec.setOnClickListener {
        curWealth.substract(ECoin.ELECTRUM, 1);
        showData()
    }
    butSilverInc.setOnClickListener {
        curWealth.add(ECoin.SILVER, 1);
        showData()
    }
    butSilverDec.setOnClickListener {
        curWealth.substract(ECoin.SILVER, 1);
        showData()
    }
    butCupperInc.setOnClickListener {
        curWealth.add(ECoin.CUPPER, 1);
        showData()
    }
    butCupperDec.setOnClickListener {
        curWealth.substract(ECoin.CUPPER, 1);
        showData()
    }
    butOk.setOnClickListener {

        dialog.cancel()
        Log.e(APP_TAG, "onAccept: " + curWealth)
        onAccept(curWealth.total)
    }
    butCancel.setOnClickListener {
        dialog.cancel()
    }

    showData()
    dialog.show()
}
private fun dialogInfo(context: Context,
                       title: String,
                       desc: String? = null,
                       x :Float? = null,
                       y: Float? = null)
{
    if(title.isEmpty())
    {
        return
    }
    val dialog = Dialog(context)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(true)
    dialog.setContentView(R.layout.dialog_info_layout)

    val textTitle = dialog.findViewById(R.id.textInfoTitle) as TextView
    val textDesc = dialog.findViewById(R.id.textInfoDesc) as TextView

    textTitle.text = title

    if(desc == null)
    {
        textDesc.visibility = View.GONE
    }
    else
    {
        textDesc.visibility = View.VISIBLE
        textDesc.text = desc
        textDesc.movementMethod = ScrollingMovementMethod()
    }
    if(x != null && y != null)
    {
        val window: Window? = dialog.window
        val wlp = window!!.attributes

        wlp.gravity = Gravity.TOP or Gravity.START
        wlp.x = x.toInt()
        wlp.y = y.toInt()
        window.attributes = wlp
    }
    dialog.show()
}

/******************************************************************************************************************
*                                               COMPOSABLES
*******************************************************************************************************************/
@Composable
fun ShowName(context: Context, car: DNDChar)
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
        Text(text = car.name,
            modifier = Modifier.fillMaxWidth(),
            fontSize = dimensionResource(R.dimen.fontLarge).value.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = colorResource(R.color.contentFace)
        )
    }
}
@Composable
fun ShowWealth(context: Context, wealth: Long, onChange: (newWealth: Long)->Unit = { _ -> })
{
    var showWealth by rememberSaveable { mutableStateOf(wealth) }
    val dndWealth = DNDWealth(showWealth)

    Row(verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(R.color.clBrown2))
            .border(dimensionResource(R.dimen.contentStroke), colorResource(R.color.contentStroke))
            .padding(dimensionResource(R.dimen.contentPadding))
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { _ ->
                        dialogWealth(context = context,
                            wealth = showWealth,
                            onAccept = { newWealth ->
                                showWealth = newWealth
                                onChange(newWealth)
                            })
                    }
                )
            }
    )
    {
        coinTypes.forEachIndexed { index, type ->
            Text(
                text = dndWealth.get(type).toString(),
                fontSize = dimensionResource(R.dimen.dimFontWealth).value.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.clWhite)
            )
            Image(
                modifier = Modifier
                    .height(dimensionResource(R.dimen.dimFontWealth).value.dp)
                    .padding(dimensionResource(R.dimen.dimCoinPadding), 0.dp),
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
fun ShowRacials(context: Context, car: DNDChar)
{
    var traits = ArrayList<DEntry>()

    car.racialTraits.forEach {  item ->
        traits.add(DEntry(value = Res.getLocale(item), title = Res.getLocale(item), desc = Res.getLocale(car.race, item)))
    }

    Text(text = Res.getLocale("racials") + ": ",
        fontSize = dimensionResource(R.dimen.dimFontRacials).value.sp,
        fontWeight = FontWeight.Bold
    )
    Row(modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically)
    {
        traits.forEach { entry ->

            Box(modifier = Modifier
                .background(colorResource(R.color.racialBg))
                .padding(dimensionResource(R.dimen.contentPadding), 0.dp)
            )
            {
                var posX by rememberSaveable { mutableStateOf(0f) }
                var posY by rememberSaveable { mutableStateOf(0f) }

                Text(text = entry.value,
                    fontSize = dimensionResource(R.dimen.dimFontRacials).value.sp,
                    fontWeight = FontWeight.Normal,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .onGloballyPositioned { coordinates ->
                            posX = coordinates.positionInWindow().x
                            posY = coordinates.positionInWindow().y }
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = { offset ->
                                    dialogInfo(context = context,
                                        title = entry.title,
                                        desc = entry.desc,
                                        x = posX + offset.x,
                                        y = posY + offset.y) }
                            )
                        },
                    color = colorResource(R.color.racialFace)
                )
            }
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.contentSpaceX)))
        }
    }
}
@Composable
fun ShowBasics(context: Context, car: DNDChar)
{
    var arrSimple : ArrayList<DEntry> = ArrayList()

    arrSimple.add(DEntry(Res.getLocale("height") + ": ", car.height.toString()))
    arrSimple.add(DEntry(Res.getLocale("weight") + ": ", car.weight.toString()))
    arrSimple.add(DEntry(Res.getLocale("age") + ": ", car.age.toString() + " " + Res.getLocale("years")))
    arrSimple.add(DEntry(Res.getLocale("alignment") + ": ", Res.getLocale(car.alignment)))
    arrSimple.add(DEntry(Res.getLocale("speed") + ": ", car.speed.toString()))
    arrSimple.add(DEntry(Res.getLocale("backgroud") + ": ", Res.getLocale(car.background), Res.getLocale(car.background), Res.getLocale(car.background, "desc")))

    Column(modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top)
    {
        // Características básicas
        arrSimple.forEach { entry ->
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically)
            {
                Text(text = entry.key,
                    fontSize = dimensionResource(R.dimen.dimFontBasics).value.sp,
                    fontWeight = FontWeight.Bold
                )
                if(entry.title.isEmpty())
                {
                    Text(text = entry.value,
                        fontSize = dimensionResource(R.dimen.dimFontBasics).value.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
                else
                {
                    Box(modifier = Modifier
                        .background(colorResource(R.color.racialBg))
                        .padding(dimensionResource(R.dimen.contentPadding), 0.dp)
                    )
                    {
                        var posX by rememberSaveable { mutableStateOf(0f) }
                        var posY by rememberSaveable { mutableStateOf(0f) }

                        Text(text = entry.value,
                            fontSize = dimensionResource(R.dimen.dimFontRacials).value.sp,
                            fontWeight = FontWeight.Normal,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier
                                .onGloballyPositioned { coordinates ->
                                    posX = coordinates.positionInWindow().x
                                    posY = coordinates.positionInWindow().y }
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onTap = { offset ->
                                            dialogInfo(context = context,
                                                title = entry.title,
                                                desc = entry.desc,
                                                x = posX + offset.x,
                                                y = posY + offset.y) }
                                    )
                                },
                            color = colorResource(R.color.racialFace)
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun ShowRoundContent(modifier: Modifier = Modifier, size : Dp,
                     bitmap: ImageBitmap? = null,
                     number: Int? = null,
                     tint : Boolean = true,
                     onClick: (x: Float, y: Float)->Unit = { _, _ -> },
                     onLongClick: (x: Float, y: Float)->Unit = { _, _ -> })
{
    var gPosX by rememberSaveable { mutableStateOf(0f) }
    var gPosY by rememberSaveable { mutableStateOf(0f) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(colorResource(R.color.contentBg))
            .padding(dimensionResource(R.dimen.contentStroke))
            .border(
                shape = CircleShape,
                width = dimensionResource(R.dimen.contentStroke),
                color = colorResource(R.color.contentFace)
            )
            .padding(dimensionResource(R.dimen.contentStroke))
            .onGloballyPositioned { coordinates ->
                gPosX = coordinates.positionInWindow().x
                gPosY = coordinates.positionInWindow().y
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { offset -> onClick(gPosX + offset.x, gPosY + offset.y) },
                    onLongPress = { offset -> onLongClick(gPosX + offset.x, gPosY + offset.y) }
                )
            }
    )
    {
        if(number != null)
        {
            Text(
                text = number.toString(),
                textAlign = TextAlign.Center,
                fontSize = dimensionResource(R.dimen.dimLevelFont).value.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = colorResource(R.color.contentFace)
            )
        }
        else if(bitmap != null)
        {
            Image(contentDescription = null,
                bitmap = bitmap,
                contentScale = ContentScale.Fit,
                colorFilter = if(tint){ ColorFilter.tint(colorResource(R.color.contentFace), BlendMode.SrcAtop) } else { null },
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)

            )
        }
    }
}
