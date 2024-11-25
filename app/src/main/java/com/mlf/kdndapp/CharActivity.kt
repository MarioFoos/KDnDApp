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
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
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
import kotlin.math.roundToInt

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

        val race = ERace.ROCK_GNOME//Res.randomItem(ERace.values())
        val klass = Res.randomItem(EClass.values())
        val stageOfLife = Res.randomItem(EStageOfLife.values())
        val ethnicity = Res.randomItem(EEthnicity.values())
        val gender = Res.randomItem(EGender.values())
        val name = Res.genName(race, ethnicity, gender, stageOfLife)

        val car = DNDChar(name, race, klass)
        car.genAge(stageOfLife)
        car.setWealth(ComTools.random(30L, 500L))
        car.alignment = Res.randomItem(EAlignment.values())
        car.addFeat(EFeat.LUCKY)

        var ab = 10
        for (ability in EAbility.values()) {
            car.setAbilityBase(ability, ab++)
        }
        car.setAbilityRacialsBonus(EAbility.CHARISMA, EAbility.INTELLIGENCE)
        car.setAbilityExtraBonus(EAbility.STRENGTH, 1)
        car.background = EBackground.ACOLYTE

        setContent {
            Box(modifier = Modifier
                .fillMaxSize()
                .paint(painterResource(id = R.drawable.bg_hoja_dnd), contentScale = ContentScale.FillBounds)
                .focusable(true)
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
                ShowWealth(car = car)
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
                            onClick = { offset ->
                                dialogInfo(this@CharActivity,
                                    title = Res.getLocale(car.klass),
                                    desc = Res.getLocale(car.klass, "desc"),
                                    offset = offset)
                            })
                        ShowRoundContent(bitmap = getImageGender(car.gender), size = dimensionResource(R.dimen.dimGender),
                            modifier = Modifier.align(alignment = Alignment.BottomStart),
                            onClick = { offset ->
                                dialogInfo(context = this@CharActivity, title = Res.getLocale("gender"), offset = offset)
                            })
                        ShowRoundContent(number = car.level, size = dimensionResource(R.dimen.dimLevel),
                            modifier = Modifier.align(alignment = Alignment.TopStart),
                            onClick = { offset ->
                                dialogInfo(this@CharActivity,
                                    title = Res.getLocale("level"),
                                    desc = Res.getLocale("level_desc"),
                                    offset = offset)
                            })
                        ShowRoundContent(number = car.inspiration, size = dimensionResource(R.dimen.dimLevel),
                            modifier = Modifier.align(alignment = Alignment.TopEnd),
                            onClick = { offset ->
                                dialogInfo(this@CharActivity,
                                    title = Res.getLocale("inspiration"),
                                    desc = Res.getLocale("inspiration_desc"),
                                    offset = offset)
                            })
                    }
                    SpaceH()
                    Column(modifier = Modifier.fillMaxWidth())
                    {
                        ShowBasics(car)
                    }
                }
                SpaceV()
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth())
                {
                    ShowRacials(car = car, context = this@CharActivity)
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
private fun dialogInfo(context: Context, title: String = "", desc: String = "", offset: Offset? = null)
{
    val dialog = Dialog(context)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(true)
    dialog.setContentView(R.layout.dialog_info_layout)

    val textTitle = dialog.findViewById(R.id.textInfoTitle) as TextView
    val textDesc = dialog.findViewById(R.id.textInfoDesc) as TextView
    if(title.isEmpty() && desc.isEmpty())
    {
        Log.e(APP_TAG, "dialogInfo no title and no text")
        return
    }
    if(title.isEmpty())
    {
        textTitle.visibility = View.GONE
    }
    else
    {
        textTitle.visibility = View.VISIBLE
        textTitle.text = title
    }
    if(desc.isEmpty())
    {
        textDesc.visibility = View.GONE
    }
    else
    {
        textDesc.visibility = View.VISIBLE
        textDesc.text = desc
        textDesc.movementMethod = ScrollingMovementMethod()
        //        tv1.setMovementMethod(new ScrollingMovementMethod());
    }
    if(offset != null)
    {
        val wmlp = dialog.window!!.attributes
        wmlp.gravity = Gravity.TOP or Gravity.START
        wmlp.x = offset.x.roundToInt()
        wmlp.y = offset.y.roundToInt()
    }
    dialog.show()
}

/******************************************************************************************************************
*                                               COMPOSABLES
*******************************************************************************************************************/
@Composable
fun ShowProfile(car: DNDChar, context: Context)
{

}
@Composable
fun ShowRacials(car: DNDChar, context: Context)
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

            /*Box(modifier = Modifier
                .background(colorResource(R.color.contentBg))
                .border(dimensionResource(R.dimen.contentStroke), colorResource(R.color.contentStroke))
                .padding(dimensionResource(R.dimen.textPaddingX), dimensionResource(R.dimen.textPaddingY))
            )*/
            Box(modifier = Modifier
                .background(colorResource(R.color.racialBg))
                //.border(dimensionResource(R.dimen.contentStroke), colorResource(R.color.contentStroke))
                .padding(dimensionResource(R.dimen.textPaddingX), 0.dp /*dimensionResource(R.dimen.textPaddingY)*/)
            )
            {
                Text(text = entry.value,
                    fontSize = dimensionResource(R.dimen.dimFontRacials).value.sp,
                    fontWeight = FontWeight.Normal,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = { offset -> dialogInfo(context, entry.title, entry.desc, offset) }
                            )
                        },
                    color = colorResource(R.color.racialFace)
                )
                /*Text(text = entry.value,
                    fontSize = dimensionResource(R.dimen.dimFontRacials).value.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = { offset -> dialogInfo(context, entry.title, entry.desc, offset) }
                            )
                        },
                    color = colorResource(R.color.contentFace)
                )*/
            }
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.contentSpaceX)))
        }
    }
}
@Composable
fun ShowBasics(car: DNDChar)
{
    var arrSimple : ArrayList<DEntry> = ArrayList()

    arrSimple.add(DEntry(Res.getLocale("height") + ": ", car.height.toString()))
    arrSimple.add(DEntry(Res.getLocale("weight") + ": ", car.weight.toString()))
    arrSimple.add(DEntry(Res.getLocale("age") + ": ", car.age.toString() + " " + Res.getLocale("years")))
    arrSimple.add(DEntry(Res.getLocale("alignment") + ": ", Res.getLocale(car.alignment)))
    arrSimple.add(DEntry(Res.getLocale("speed") + ": ", car.speed.toString()))

    Column(modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top)
    {
        // Nombre
        Text(
            text = car.name,
            modifier = Modifier.fillMaxWidth(),
            fontSize = dimensionResource(R.dimen.fontText).value.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        // Características básicas
        arrSimple.forEachIndexed { index, dEntry ->
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Top)
            {
                Text(text = arrSimple[index].key,
                    fontSize = dimensionResource(R.dimen.dimFontBasics).value.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(text = arrSimple[index].value,
                    fontSize = dimensionResource(R.dimen.dimFontBasics).value.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}
@Composable
fun ShowWealth(car: DNDChar)
{
    Row(verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(R.color.clBrown2))
            .border(dimensionResource(R.dimen.contentStroke), colorResource(R.color.contentStroke))
            .padding(dimensionResource(R.dimen.contentPadding))
    )
    {
        coinTypes.forEachIndexed { index, type ->
            Text(
                text = car.wealth.get(type).toString(),
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
fun ShowRoundContent(modifier: Modifier = Modifier, size : Dp,
                     bitmap: ImageBitmap? = null,
                     number: Int? = null,
                     tint : Boolean = true,
                     onClick: (offset: Offset)->Unit = { _ -> },
                     onLongClick: (offset: Offset)->Unit = { _ -> })
{
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
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { offset -> onClick(offset) },
                    onLongPress = { offset -> onLongClick(offset) }
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
