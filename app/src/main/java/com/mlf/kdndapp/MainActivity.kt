package com.mlf.kdndapp

import android.content.res.AssetManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dndlib.base.EAbility
import com.dndlib.base.EAlignment
import com.dndlib.base.EClass
import com.dndlib.base.EFeat
import com.dndlib.base.EGender
import com.dndlib.base.ERace
import com.dndlib.base.EStageOfLife
import com.dndlib.base.Res
import com.mlf.kdndapp.ui.theme.KDnDAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

val APP_TAG = "AppTag"

class MainActivity : ComponentActivity()
{
    var test = 0

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
        val arrRace : ArrayList<Map.Entry<ERace, String>> = ERace.getLocale()
        val arrClass : ArrayList<Map.Entry<EClass, String>> = EClass.getLocale()
        val arrAlign : ArrayList<Map.Entry<EAlignment, String>> = EAlignment.getLocale()
        val arrGender : ArrayList<Map.Entry<EGender, String>> = EGender.getLocale()
        val arrStage : ArrayList<Map.Entry<EStageOfLife, String>> = EStageOfLife.getLocale()
        val arrFeat : ArrayList<Map.Entry<EFeat, String>> = EFeat.getLocale()
        val arrAbility : ArrayList<Map.Entry<EAbility, String>> = EAbility.getLocale()

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
                            ShowButton(text = age.toString() + " años", onItemClick = { age = ERace.genAge(race, stage)  })
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
                            ShowButton(text = height.toString() + " m", onItemClick = { height = ERace.genHeight(race) })
                        }
                        SpaceH()
                        // Peso
                        Column(Modifier.weight(1f, true)) {
                            ShowButton(text = weight.toString() + " kg", onItemClick = { weight = ERace.genWeight(race) })
                        }
                    }
                    SpaceV()
                    ShowRacialAbilities(race = race)
                    if(race == ERace.VARIANT_HUMAN)
                    {
                        ShowDropDown(arrFeat, initIndex = featInit, onItemClick = { feat = arrFeat[it].key })
                        SpaceV()
                        ShowFeat(feat = feat)
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
                    }
                    SpaceV()
                    ShowDropDown(arrClass, initIndex = classInit, onItemClick = { klass = arrClass[it].key }, true)
                    SpaceV()
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
fun SpaceV() = Spacer(modifier = Modifier.height(6.dp))

@Composable
fun SpaceH() = Spacer(modifier = Modifier.width(6.dp))

@Composable
fun ShowFeat(feat : EFeat)
{
    var data = Res.getFeatLocale(feat)
    var prerequisite : String = data[1]
    var desc : String = data[2]

    if(prerequisite.isNotEmpty())
    {
        Text(text = prerequisite,
            modifier = Modifier.fillMaxWidth()
                .padding(2.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic
        )
    }
    Text(text = desc,
        modifier = Modifier.fillMaxWidth()
            .padding(2.dp),
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )
    SpaceV()
}
@Composable
fun ShowRacialAbilities(race : ERace)
{
    var racials = ERace.getRacials(race)
    if(racials.isEmpty())
    {
        return
    }
    var info1 : String = ""
    var info2 : String = ""
    racials.entries.forEachIndexed{index, entry ->
        if(index %2 == 0)
        {
            info1 += Res.getString(entry.key) + " (+" + entry.value + ")"
            if(index + 2 < racials.entries.size)
            {
                info1 += "\n"
            }
        }
        else
        {
            info2 += Res.getString(entry.key) + " (+" + entry.value + ")"
            if(index + 2 < racials.entries.size)
            {
                info2 += "\n"
            }
        }
    }
    if(info1.isEmpty() && info2.isEmpty())
    {
        return;
    }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = info1,
            modifier = Modifier
                .padding(2.dp)
                .weight(1f, true),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(text = info2,
            modifier = Modifier
                .padding(2.dp)
                .weight(1f, true),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
    SpaceV()
}
@Composable
fun ShowButton(text: String, onItemClick: () -> Unit, enabled : Boolean = true)
{
    val strokeColor = if(enabled)  colorResource(id = R.color.but_brown_enabled_border) else colorResource(id = R.color.yellow_dark)

    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = onItemClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            colorResource(id = R.color.but_brown_enabled_bg),
            colorResource(id = R.color.but_brown_enabled_text),
            colorResource(id = R.color.but_brown_dis_bg),
            colorResource(id = R.color.but_brown_dis_text)
        ),
        border = BorderStroke(dimensionResource(id = R.dimen.but_stroke), strokeColor),
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(0.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
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
        ShowButton(text = items[itemPosition.value].value, onItemClick = { menuExpanded.value = true }, enabled = enabled)
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

/*********************************************************************************************************************************/

@Composable
fun MyTextView(data1: String, modifier: Modifier = Modifier) {
    Column(
        // we are using column to align
        // our textview to center of the screen.
        modifier = Modifier.fillMaxSize(),

        // below line is used for specifying
        // horizontal arrangement.
        horizontalAlignment = Alignment.CenterHorizontally,

        // below line is used for specifying
        // vertical arrangement.
        verticalArrangement = Arrangement.Center
    ){
        // This is Text composable function
        Text(text = data1, modifier = modifier)
    }
}

@Composable
fun ComposableFunctionColumns(){
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Text(
            text = "Hola mundo",
            color = Color.White,
            fontFamily = FontFamily.SansSerif,
            fontSize = 30.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center,
            textDecoration = TextDecoration.None,
            maxLines = 1,
            modifier = Modifier
                .clickable {
            Toast.makeText(context, "Test", Toast.LENGTH_LONG).show()
        })
    }
}

@Composable
fun TextExample() {
    Column(
        // we are using column to align
        // our textview to center of the screen.
        modifier = Modifier.fillMaxSize(),

        // below line is used for specifying
        // horizontal arrangement.
        horizontalAlignment = Alignment.CenterHorizontally,

        // below line is used for specifying
        // vertical arrangement.
        verticalArrangement = Arrangement.Center
    ) {
        // Text Composable
        Text(
            // below line is for displaying
            // text in our text.
            text = "Botón",

            // modifier is use to give
            // padding to our text.
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

            // color is used to provide white
            // color to our text.
            color = Color.White,

            // font size to change the
            // size of our text.
            fontSize = 40.sp,

            // font style is use to change style
            // of our text to italic and normal.
            fontStyle = FontStyle.Italic,

            // font weight to bold such as light bold,
            // extra bold to our text.
            fontWeight = FontWeight.Bold,

            // font family is use to change
            // our font family to SansSerif.
            fontFamily = FontFamily.SansSerif,

            // letter spacing is use to
            // provide between letters.
            letterSpacing = 1.5.sp,

            // Decorations applied to the text (Underline)
            textDecoration = TextDecoration.Underline,

            // textAlign to align our text view
            // to center position.
            textAlign = TextAlign.Center,

            // The height of each line of text (24sp).
            lineHeight = 24.sp,

            // Used to handle overflowed text (Ellipsis).
            overflow = TextOverflow.Ellipsis,

            // Whether the text should wrap if it exceeds the width of its container (true).
            softWrap = true,

            // The maximum number of lines for the text (2).
            maxLines = 2,

            // A callback that is invoked when the text is laid out, here used to print the line count.
            onTextLayout = { textLayoutResult: TextLayoutResult ->
                // Example usage of onTextLayout callback
                val lineCount = textLayoutResult.lineCount
                println("Line Count: $lineCount")
            },

            // below line is used to add
            // style to our text view.
            style = TextStyle(

                // background is use to specify background
                // color to our text view.
                background = Color.Green,

                // shadow to make our
                // text view elevated.
                shadow = Shadow(color = Color.Gray, blurRadius = 40f)
            )
        )
    }
}

@Composable
fun MyButton() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val context = LocalContext.current

        Button(
            onClick = {
                Toast.makeText(context, "Welcome to Geeks for Geeks", Toast.LENGTH_LONG).show()
            },
            modifier = Modifier.padding(16.dp),
            enabled = true,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = Color.Green
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp),
            border = BorderStroke(width = 2.dp, brush = SolidColor(Color.Blue)),
            contentPadding = PaddingValues(
                start = 20.dp,
                top = 12.dp,
                end = 20.dp,
                bottom = 12.dp
            ),
            interactionSource = remember { MutableInteractionSource() }
        ) {
            Text(
                text = "Geeks for Geeks",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                fontFamily = FontFamily.Serif
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTextFieldUI() {
    // Remember the text entered in the TextField
    var text by rememberSaveable { mutableStateOf("") }

    // Layout to organize the TextField and the Text below it
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TextField for user input
        TextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Enter your text") },
            placeholder = { Text("Texto de ejemplo") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Displaying the text entered by the user
        Text(text = "You entered: $text")
    }
}

// Creating a composable function to
// create two Images and a spacer between them
// Calling this function as content
// in the above function
@Composable
fun MyContent(){

    // Fetching the Local Context
    val mContext = LocalContext.current

    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {

        // Declaring a boolean value for storing checked state
        val mCheckedState = remember{ mutableStateOf(false)}

        // Creating a Switch, when value changes,
        // it updates mCheckedState value
        Switch(checked = mCheckedState.value, onCheckedChange = {mCheckedState.value = it})

        // Adding a Space of 100dp height
        Spacer(modifier = Modifier.height(100.dp))

        // Creating a Button to display mCheckedState
        // value in a Toast when clicked
        Button(onClick = {
            Toast.makeText(mContext, mCheckedState.value.toString(), Toast.LENGTH_SHORT).show()
        },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0XFF0F9D58)),
        ) {
            Text("Show Checked State", color = Color.White)
        }
    }
}

@Composable
fun SimpleRadioButtonComponent() {
    val radioOptions = listOf("DSA", "Java", "C++")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[2]) }
    Column(
        // we are using column to align our
        // radio buttons to center of the screen.
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),

        // below line is used for
        // specifying vertical arrangement.
        verticalArrangement = Arrangement.Center,

        // below line is used for
        // specifying horizontal arrangement.
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // we are displaying all our
        // radio buttons in column.
        radioOptions.forEach { text ->
            Row(
                Modifier
                    // using modifier to add max
                    // width to our radio button.
                    .fillMaxWidth()

                    // below method is used to add
                    // selectable to our radio button.
                    .selectable(
                        // this method is called when
                        // radio button is selected.
                        selected = (text == selectedOption),

                        // below method is called on
                        // clicking of radio button.
                        onClick = { onOptionSelected(text) }
                    )

                    // below line is used to add
                    // padding to radio button.
                    .padding(horizontal = 16.dp),

                // below line is used for
                // specifying horizontal arrangement.
                horizontalArrangement = Arrangement.Start,

                // below line is used for
                // specifying vertical arrangement.
                verticalAlignment = Alignment.CenterVertically
            ) {
                // below line is used to get context.
                val context = LocalContext.current

                // below line is used to
                // generate radio button
                RadioButton(
                    // inside this method we are
                    // adding selected with a option.
                    selected = (text == selectedOption),

                    onClick = {
                        // inside on click method we are setting a
                        // selected option of our radio buttons.
                        onOptionSelected(text)

                        // after clicking a radio button
                        // we are displaying a toast message.
                        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
                    },

                    // below line is used to Add padding around the RadioButton to separate it from other UI elements.
                    modifier = Modifier.padding(8.dp),

                    // below line is used to make the RadioButton is enabled, allowing it to be clickable.
                    enabled = true,

                    // below line is used for customizing colors in RadioButton
                    colors = RadioButtonDefaults.colors(
                        Color.Green,
                        Color.Blue
                    ),

                    // below line is uses a default MutableInteractionSource to handle interaction states.
                    interactionSource = remember { MutableInteractionSource() }
                )

                // below line is used to add
                // text to our radio buttons.
                Text(
                    text = text,
                    modifier = Modifier.padding(start = 16.dp),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun SimpleCheckboxComponent() {
    // in below line we are setting
    // the state of our checkbox.
    val isChecked = remember { mutableStateOf(false) }

    // in below line we are displaying a Column
    // and we are creating a checkbox in a Column.
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Checkbox(
            // below line we are setting
            // the state of checkbox.
            checked = isChecked.value,

            // below line is used to add on check
            // change to our checkbox.
            onCheckedChange = { isChecked.value = it },

            // below line is used to add padding
            // to our checkbox.
            modifier = Modifier.padding(8.dp),

            // below line is used to add A boolean value that
            // determines whether the checkbox is enabled.
            enabled = true,

            // below line is used to add the colors
            // to the checkbox
            colors = CheckboxDefaults.colors(
                checkedColor = Color.Green,
                uncheckedColor = Color.Red,
                checkmarkColor = Color.White
            ),

            //below line is uses an interaction source
            // that handles interaction events for the checkbox
            interactionSource = remember { MutableInteractionSource() }
        )
        // below line is used to add text to our check box and we are
        // adding padding to our text of checkbox
        Text(
            text = if (isChecked.value) "Checked" else "Unchecked",
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
@Composable
fun SnackBars() {
    Text(text = "Snackbars", style = MaterialTheme.typography.headlineLarge, modifier = Modifier.padding(8.dp))
    // Normal Snackbar
    Snackbar(modifier = Modifier.padding(4.dp)) {
        Text(text = "This is a basic snackbar")
    }
    // Snackbar with action item
    Snackbar(
        modifier = Modifier.padding(4.dp),
        action = {
            TextButton(onClick = {}) {
                Text(text = "Remove")
            }
        }
    ) {
        Text(text = "This is a basic Snackbar with action item")
    }

    // Snackbar with action item below text
    Snackbar(
        modifier = Modifier.padding(4.dp),
        actionOnNewLine = true,
        action = {
            TextButton(onClick = {}) {
                Text(text = "Remove")
            }
        }
    ) {
        Text(text = "Snackbar with action item below text")
    }
}
@Composable
fun ActionTriggeredSnackbar(scope : CoroutineScope, snackbarHostState : SnackbarHostState) {
    Column {
        // Button: It triggers the temporary snack bar to show up.
        Button(onClick = {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "Temporary Snackbar",
                    actionLabel = "Dismiss",
                    duration = SnackbarDuration.Short
                )
            }
        }) {
            Text(
                text = "Show Temporary Snackbar"
            )
        }
    }
}