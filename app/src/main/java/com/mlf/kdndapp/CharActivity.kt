package com.mlf.kdndapp

import android.app.Dialog
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Window
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dndlib.DNDChar
import com.dndlib.base.EAbility
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

class CharActivity : ComponentActivity()
{
    private val coinIcons = arrayOf(R.drawable.coin_platinum, R.drawable.coin_gold,  R.drawable.coin_electrum, R.drawable.coin_silver, R.drawable.coin_cupper)
    private val coinTypes = arrayOf(ECoin.PLATINUM, ECoin.GOLD, ECoin.ELECTRUM, ECoin.SILVER, ECoin.CUPPER)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var offset: Offset = Offset(0f, 0f)

        val race = Res.randomItem(ERace.values())
        val klass = Res.randomItem(EClass.values())
        val stageOfLife = Res.randomItem(EStageOfLife.values())
        val ethnicity = Res.randomItem(EEthnicity.values())
        val gender = Res.randomItem(EGender.values())
        val name = Res.genName(race, ethnicity, gender, stageOfLife)

        val car = DNDChar(name, race, klass)
        car.genAge(stageOfLife)
        car.setWealth(ComTools.random(30L, 500L))
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
                .clickable(enabled = true, onClick = { Log.e(APP_TAG, "clic box") })
            ){}
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(R.dimen.dimMainPadding))
                    .verticalScroll(rememberScrollState())
                    .pointerInput(Unit) { detectTapGestures { offset = it } },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // Oro
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                        .background(colorResource(R.color.clBrown1)))
                {
                    coinTypes.forEachIndexed {
                        index, type ->
                        Text(
                            modifier = Modifier.padding(2.dp),
                            text = " " + car.wealth.get(type).toString(),
                            fontSize = dimensionResource(R.dimen.dimTextFont).value.sp,
                            color = colorResource(id = R.color.clWhite))
                        Image(
                            modifier = Modifier.padding(2.dp),
                            painter = painterResource(id = coinIcons[index]),
                            contentDescription = null)
                    }
                }
                // Raza, clase, género
                // Básicos
                Row(verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .height(140.dp)
                        .fillMaxWidth())
                {
                    Box(contentAlignment = Alignment.Center,
                        modifier = Modifier.size(dimensionResource(R.dimen.dimProfile))
                    )
                    {
                        ShowRoundContent(bitmap = getImageRace(race = car.race, gender = car.gender),
                            size = dimensionResource(R.dimen.dimProfile), tint = false)

                        ShowRoundContent(bitmap = getImageKlass(car.klass), size = dimensionResource(R.dimen.dimKlass),
                            modifier = Modifier.align(alignment = Alignment.BottomEnd))

                        ShowRoundContent(bitmap = getImageGender(car.gender), size = dimensionResource(R.dimen.dimGender),
                            modifier = Modifier.align(alignment = Alignment.BottomStart))

                        ShowRoundContent(number = car.level, size = dimensionResource(R.dimen.dimLevel),
                            modifier = Modifier.align(alignment = Alignment.TopStart))
                    }
                    Column(modifier = Modifier.fillMaxWidth())
                    {
                        Text(
                            text = car.name,
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = dimensionResource(R.dimen.dimTextFont).value.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                        )
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusable(true),
                            contentPadding = PaddingValues(8.dp, 0.dp),
                            onClick = { },
                            enabled = true,
                            colors = ButtonDefaults.buttonColors(
                                colorResource(R.color.clButBrownEnBg),
                                colorResource(R.color.clButBrownEnText),
                                colorResource(R.color.clButBrownDisBg),
                                colorResource(R.color.clButBrownDisText)),
                            border = BorderStroke(dimensionResource(R.dimen.dimButStroke),
                                colorResource(R.color.clButBrownEnStroke)),
                            shape =  controlShape
                        ) {}
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

private fun dialogInfo(context: Context, title: String, text: String,
                       posx : Int = -1, posy : Int = -1) {
    val dialog = Dialog(context)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(true)
    dialog.setContentView(R.layout.dialog_info_layout)

    val body = dialog.findViewById(R.id.textInfoTitle) as TextView
    body.text = text

    if(posx >= 0 && posy >= 0)
    {
        val wmlp = dialog.window!!.attributes
        wmlp.gravity = Gravity.TOP or Gravity.START
        wmlp.x = posx
        wmlp.y = posy
    }

    dialog.show()
}

/******************************************************************************************************************
*                                               COMPOSABLES
*******************************************************************************************************************/

@Composable
fun ShowRoundContent(modifier: Modifier = Modifier, size : Dp,
                     bitmap: ImageBitmap? = null,
                     number: Int = 0,
                     tint : Boolean = true,
                     onClick: ()->Unit = {})
{
    /*
        dialogInfo(context = this@CharActivity,
            posx = offset.x.roundToInt(), posy = offset.y.roundToInt(),
            title = "Titulo",
            text = "Texto de prueba")
    */

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(colorResource(R.color.clContentBg))
            .border(
                shape = CircleShape,
                width = dimensionResource(R.dimen.dimStrokeContent),
                color = colorResource(R.color.clContentStroke)
            )
            .padding(dimensionResource(R.dimen.dimStrokeContent)),
    )
    {
        //
        if(number > 0)
        {
            Text(
                text = number.toString(),
                textAlign = TextAlign.Center,
                fontSize = dimensionResource(R.dimen.dimLevelFont).value.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = colorResource(R.color.clContentStroke)
            )
        }
        else if(bitmap != null)
        {
            Image(contentDescription = null,
                bitmap = bitmap,
                contentScale = ContentScale.Fit,
                colorFilter = if(tint){ ColorFilter.tint(colorResource(R.color.clYellow4), BlendMode.SrcAtop) } else { null },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
