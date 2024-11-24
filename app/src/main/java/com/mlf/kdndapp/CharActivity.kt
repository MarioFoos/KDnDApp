package com.mlf.kdndapp

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dndlib.DNDChar
import com.dndlib.DNDCharManager
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
import com.mlf.kdndapp.ui.theme.*


class CharActivity : ComponentActivity()
{
    private val coinIcons = arrayOf(R.drawable.coin_platinum, R.drawable.coin_gold,  R.drawable.coin_electrum, R.drawable.coin_silver, R.drawable.coin_cupper)
    private val coinTypes = arrayOf(ECoin.PLATINUM, ECoin.GOLD, ECoin.ELECTRUM, ECoin.SILVER, ECoin.CUPPER)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //val name = intent?.extras?.getString("char").toString()

        val race = Res.randomItem(ERace.values())
        val klass = Res.randomItem(EClass.values())
        val stageOfLife = Res.randomItem(EStageOfLife.values())
        val ethnicity = Res.randomItem(EEthnicity.values())
        val gender = Res.randomItem(EGender.values())
        val name = Res.genName(race, ethnicity, gender, stageOfLife)

        val car = DNDChar(name, race, klass)
        car.genAge(stageOfLife)
        car.setWealth(ComTools.random(30L, 500L))

        var ab = 10

        car.addFeat(EFeat.LUCKY)
        for (ability in EAbility.values()) {
            car.setAbilityBase(ability, ab++)
        }
        car.setAbilityRacialsBonus(EAbility.CHARISMA, EAbility.INTELLIGENCE)
        car.setAbilityExtraBonus(EAbility.STRENGTH, 1)
        car.background = EBackground.ACOLYTE

        val strChar = DNDCharManager.toString(car)
        Log.e(APP_TAG, "--------------- JSON ---------------------")
        Log.e(APP_TAG, strChar)

        setContent {
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
                    .fillMaxSize()
                    .padding(dimMainPadding)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // Oro
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                        .background(clBrown1))
                {
                    coinTypes.forEachIndexed {
                        index, type ->
                        Text(
                            modifier = Modifier.padding(2.dp),
                            text = " " + car.wealth.get(type).toString(), fontSize = dimTextFont, color = clWhite)
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
                        modifier = Modifier.size(140.dp)
                    )
                    {
                        ShowRoundImage(bitmap = getImageRace(race = car.race, gender = car.gender), size = 140.dp, tint = false)

                        ShowRoundImage(bitmap = getImageKlass(car.klass), size = 50.dp,
                            modifier = Modifier.align(alignment = Alignment.BottomEnd))

                        ShowRoundImage(bitmap = getImageGender(car.gender), size = 40.dp,
                            modifier = Modifier.align(alignment = Alignment.BottomStart))

                    }
                    Column(modifier = Modifier.fillMaxWidth())
                    {
                        Text(text = "Nombre",
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = dimTextFont,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
    }
    fun getImageGender(gender : EGender) : ImageBitmap
    {
        var file = "imgs/gender/" + gender.name.lowercase() + ".png"
        var img = BitmapFactory.decodeStream(assets.open(file))
        return img.asImageBitmap()
    }
    fun getImageKlass(klass : EClass) : ImageBitmap
    {
        var file = "imgs/klass/" + klass.name.lowercase() + ".png"
        var img = BitmapFactory.decodeStream(assets.open(file))
        return img.asImageBitmap()
    }
    fun getImageRace(race : ERace, gender : EGender) : ImageBitmap
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

        Log.e(APP_TAG, "----> IMG: " + file)

        var img = BitmapFactory.decodeStream(assets.open(file))
        return img.asImageBitmap()
    }
}
@Composable
fun ShowRoundImage(modifier: Modifier = Modifier,
                   bitmap: ImageBitmap,
                   size : Dp,
                   stroke : Dp = dimStrokeContent,
                   tint : Boolean = true)
{


    Box(contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(clContentBg)
            .border(shape = CircleShape, width = stroke, color = clContentStroke)
            .padding(stroke),
    )
    {
        Image(contentDescription = null,
            bitmap = bitmap,
            contentScale = ContentScale.Fit,
            colorFilter = if(tint){ ColorFilter.tint(clYellow4, BlendMode.SrcAtop) } else { null },
            modifier = Modifier.fillMaxSize()
        )
    }
}













