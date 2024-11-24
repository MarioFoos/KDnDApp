package com.mlf.kdndapp

import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import com.dndlib.DNDChar
import com.dndlib.DNDCharManager
import com.dndlib.base.EAbility
import com.dndlib.base.EBackground
import com.dndlib.base.EClass
import com.dndlib.base.EFeat
import com.dndlib.base.EGender
import com.dndlib.base.ERace
import com.mlf.kdndapp.ui.theme.clBrown1
import com.mlf.kdndapp.ui.theme.clBrown2
import com.mlf.kdndapp.ui.theme.clBrown3
import com.mlf.kdndapp.ui.theme.clBrown5
import com.mlf.kdndapp.ui.theme.clWhite
import com.mlf.kdndapp.ui.theme.clYellow3
import com.mlf.kdndapp.ui.theme.clYellow4
import com.mlf.kdndapp.ui.theme.dimButStroke
import com.mlf.kdndapp.ui.theme.dimMainPadding
import com.mlf.kdndapp.ui.theme.dimTextFont


class CharActivity : ComponentActivity()
{
    var imageCharacted : Drawable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //val name = intent?.extras?.getString("char").toString()

        val car = DNDChar("Nestor", ERace.DUERGAR, EClass.FIGHTER)
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

        val fileKlass = "klass/" + car.klass.name.lowercase() + ".png"
        val fileGender = "gender/" + car.gender.name.lowercase() + ".png"

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
                    val coins = arrayOf(R.drawable.coin_platinum, R.drawable.coin_gold,  R.drawable.coin_electrum, R.drawable.coin_silver, R.drawable.coin_cupper)

                    coins.forEach {
                        Text(
                            modifier = Modifier.padding(2.dp),
                            text = " 12", fontSize = dimTextFont, color = clWhite)
                        Image(
                            modifier = Modifier.padding(2.dp),
                            painter = painterResource(id = it),
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
                        Image(contentDescription = "",
                            bitmap = getImageRace(race = car.race, gender = car.gender),
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .border(shape = CircleShape, width = dimButStroke, color = clBrown3),
                        )
                        Image(contentDescription = "",
                            bitmap = getImageKlass(klass = car.klass),
                            contentScale = ContentScale.Fit,
                            colorFilter = ColorFilter.tint(clYellow4, BlendMode.SrcAtop),
                            modifier = Modifier
                                .size(50.dp).
                                align(alignment = Alignment.BottomEnd)
                                .clip(CircleShape).background(clBrown2)
                                .border(shape = CircleShape, width = 3.dp, color = clYellow4),
                        )
                        Image(contentDescription = "",
                            bitmap = getImageGender(gender = car.gender),
                            contentScale = ContentScale.Fit,
                            colorFilter = ColorFilter.tint(clYellow4, BlendMode.SrcAtop),
                            modifier = Modifier
                                .size(30.dp)
                                .align(alignment = Alignment.TopEnd)
                                .clip(CircleShape).background(clBrown2)
                                .border(shape = CircleShape, width = 3.dp, color = clYellow4),
                        )
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














