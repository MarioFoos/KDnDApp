package com.mlf.kdndapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.mlf.kdndapp.ui.theme.dimMainPadding

class CharActivity : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        val name = intent?.extras?.getString("char").toString()
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
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .padding(dimMainPadding)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // Nombre
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1.8f, true)) {
                        ShowEditText(text = name, onValueChange = {  })
                    }
                    SpaceH()
                    Column(Modifier.weight(0.2f, true)) {
                        ShowButton(icon = R.drawable.d20, onItemClick = {  })
                    }
                }
            }
        }
    }
}
