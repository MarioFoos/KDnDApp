package com.mlf.kdndapp

import android.app.Dialog
import android.content.Context
import android.text.method.ScrollingMovementMethod
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.dndlib.DNDHitPoints
import com.dndlib.DNDWealth
import com.dndlib.base.ECoin
import com.dndlib.res.Res
import com.mlf.kdndapp.ui.theme.PushButton

fun dialogXp(context: Context, x: Float? = null, y: Float? = null,
             onAccept: (addXp: Int)->Unit = { _ -> })
{
    var curXp = 0
    val dialog = Dialog(context)

    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(true)
    dialog.setContentView(R.layout.dialog_xp)

    // Fijo
    val textTitle = dialog.findViewById(R.id.textTitle) as TextView
    val butOk = dialog.findViewById(R.id.butOk)  as Button
    val butCancel = dialog.findViewById(R.id.butCancel)  as Button

    textTitle.text = Res.locale("add_xp")
    butOk.text = Res.locale("but_ok")
    butCancel.text = Res.locale("but_cancel")

    // Acciones
    val textXp = dialog.findViewById(R.id.textXp) as TextView
    val butXpInc1 = dialog.findViewById(R.id.butXpInc1) as PushButton
    val butXpInc10 = dialog.findViewById(R.id.butXpInc10)  as PushButton
    val butXpInc100 = dialog.findViewById(R.id.butXpInc100)  as PushButton
    val butXpInc1000 = dialog.findViewById(R.id.butXpInc1000)  as PushButton
    val butXpDec1 = dialog.findViewById(R.id.butXpDec1)  as PushButton
    val butXpDec10 = dialog.findViewById(R.id.butXpDec10)  as PushButton
    val butXpDec100 = dialog.findViewById(R.id.butXpDec100)  as PushButton
    val butXpDec1000 = dialog.findViewById(R.id.butXpDec1000)  as PushButton

    fun showData()
    {
        textXp.text = curXp.toString()
    }
    fun changeXp(value: Int)
    {
        curXp += value
        if(curXp < 0)
        {
            curXp = 0
        }
        showData()
    }
    butXpInc1.setAction{ changeXp(1) }
    butXpInc10.setAction{ changeXp(10) }
    butXpInc100.setAction{ changeXp(100) }
    butXpInc1000.setAction{ changeXp(1000) }
    butXpDec1.setAction{ changeXp(-1) }
    butXpDec10.setAction{ changeXp(-10) }
    butXpDec100.setAction{ changeXp(-100) }
    butXpDec1000.setAction{ changeXp(-1000) }

    butOk.setOnClickListener {
        dialog.dismiss()
        onAccept(curXp)
    }
    butCancel.setOnClickListener {
        dialog.dismiss()
    }
    showData()
    showDialogXY(dialog, x, y)
}
fun dialogHp(context: Context, x: Float? = null, y: Float? = null,
             hp: DNDHitPoints,
             onAccept: (newHp: DNDHitPoints)->Unit = { _ -> })
{
    var curHp: DNDHitPoints = hp
    val dialog = Dialog(context)

    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(true)
    dialog.setContentView(R.layout.dialog_hit_points)

    // Fijo
    val textTitle = dialog.findViewById(R.id.textTitle) as TextView
    val textDescCurrent = dialog.findViewById(R.id.textDescCurrent) as TextView
    val textDescMax = dialog.findViewById(R.id.textDescMax) as TextView
    val textDescTemp = dialog.findViewById(R.id.textDescTemp) as TextView
    val butOk = dialog.findViewById(R.id.butOk)  as Button
    val butCancel = dialog.findViewById(R.id.butCancel)  as Button

    textTitle.text = Res.locale("hit_points")
    textDescCurrent.text = Res.locale("hp_currents")
    textDescMax.text = Res.locale("hp_max")
    textDescTemp.text = Res.locale("hp_temp")
    butOk.text = Res.locale("but_ok")
    butCancel.text = Res.locale("but_cancel")

    // Acciones
    val butCurrentDec = dialog.findViewById(R.id.butCurrentDec1000)  as Button
    val butCurrentInc = dialog.findViewById(R.id.butCurrentDec100)  as Button
    val butMaxDec = dialog.findViewById(R.id.butMaxDec)  as Button
    val butMaxInc = dialog.findViewById(R.id.butMaxInc)  as Button
    val butTempDec = dialog.findViewById(R.id.butTempDec)  as Button
    val butTempInc = dialog.findViewById(R.id.butTempInc)  as Button
    val textCurrent = dialog.findViewById(R.id.textCurrent) as TextView
    val textMax = dialog.findViewById(R.id.textMax) as TextView
    val textTemp = dialog.findViewById(R.id.textTemp) as TextView

    fun showData()
    {
        textCurrent.text = curHp.hp.toString()
        textMax.text = curHp.max.toString()
        textTemp.text = curHp.temp.toString()
    }
    butCurrentDec.setOnClickListener {
        curHp.hp = curHp.hp - 1
        showData()
    }
    butCurrentInc.setOnClickListener {
        curHp.hp = curHp.hp + 1
        showData()
    }
    butMaxDec.setOnClickListener {
        curHp.max = curHp.max - 1
        showData()
    }
    butMaxInc.setOnClickListener {
        curHp.max = curHp.max + 1
        showData()
    }
    butTempDec.setOnClickListener {
        curHp.temp = curHp.temp - 1
        showData()
    }
    butTempInc.setOnClickListener {
        curHp.temp = curHp.temp + 1
        showData()
    }
    butOk.setOnClickListener {
        dialog.dismiss()
        onAccept(curHp)
    }
    butCancel.setOnClickListener {
        dialog.dismiss()
    }
    showData()
    showDialogXY(dialog, x, y)
}
fun dialogEditNum(context: Context, x: Float? = null, y: Float? = null,
                  title: String,
                  value: Int,
                  onAccept: (newValue: Int)->Unit = { _ -> })
{
    var curValue = value
    val dialog = Dialog(context)

    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(true)
    dialog.setContentView(R.layout.dialog_small_number)

    val butOk = dialog.findViewById(R.id.butOk)  as Button
    val butCancel = dialog.findViewById(R.id.butCancel)  as Button
    val textTitle = dialog.findViewById(R.id.textTitle) as TextView
    val textNumber = dialog.findViewById(R.id.textCurrent) as TextView
    val butInc = dialog.findViewById(R.id.butCurrentDec100)  as Button
    val butDec = dialog.findViewById(R.id.butCurrentDec1000)  as Button

    butOk.text = Res.locale("but_ok")
    butCancel.text = Res.locale("but_cancel")
    textTitle.text = title
    textNumber.text = curValue.toString()

    fun showData()
    {
        textNumber.text = curValue.toString()
    }
    butInc.setOnClickListener {
        if(curValue < 20)
        {
            ++curValue
        }
        showData()
    }
    butDec.setOnClickListener {
        if(curValue > 1)
        {
            --curValue
        }
        showData()
    }
    butOk.setOnClickListener {
        dialog.dismiss()
        onAccept(curValue)
    }
    butCancel.setOnClickListener {
        dialog.dismiss()
    }
    showData()
    showDialogXY(dialog, x, y)
}
fun dialogWealth(context: Context, x: Float? = null, y: Float? = null,
                 wealth: Long,
                 onAccept: (newWealth: Long)->Unit = { _ -> })
{
    var curWealth = DNDWealth(wealth)

    val dialog = Dialog(context)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(true)
    dialog.setContentView(R.layout.dialog_wealth_layout)

    val butOk = dialog.findViewById(R.id.butOk)  as Button
    val butCancel = dialog.findViewById(R.id.butCancel)  as Button

    butOk.text = Res.locale("but_ok")
    butCancel.text = Res.locale("but_cancel")

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
        curWealth.add(ECoin.PLATINUM, 1)
        showData()
    }
    butPlatinumDec.setOnClickListener {
        curWealth.substract(ECoin.PLATINUM, 1)
        showData()
    }
    butGoldInc.setOnClickListener {
        curWealth.add(ECoin.GOLD, 1)
        showData()
    }
    butGoldDec.setOnClickListener {
        curWealth.substract(ECoin.GOLD, 1)
        showData()
    }
    butElectrumInc.setOnClickListener {
        curWealth.add(ECoin.ELECTRUM, 1)
        showData()
    }
    butElectrumDec.setOnClickListener {
        curWealth.substract(ECoin.ELECTRUM, 1)
        showData()
    }
    butSilverInc.setOnClickListener {
        curWealth.add(ECoin.SILVER, 1)
        showData()
    }
    butSilverDec.setOnClickListener {
        curWealth.substract(ECoin.SILVER, 1)
        showData()
    }
    butCupperInc.setOnClickListener {
        curWealth.add(ECoin.CUPPER, 1)
        showData()
    }
    butCupperDec.setOnClickListener {
        curWealth.substract(ECoin.CUPPER, 1)
        showData()
    }
    butOk.setOnClickListener {
        dialog.dismiss()
        onAccept(curWealth.total)
    }
    butCancel.setOnClickListener {
        dialog.dismiss()
    }
    showData()
    dialog.show()
}
fun dialogInfo(context: Context, x :Float? = null, y: Float? = null,
               title: String,
               desc: String? = null)
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
    val textDesc = dialog.findViewById(R.id.textSimpleDesc) as TextView

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
    showDialogXY(dialog, x, y)
}
fun dialogError(context: Context, title: String, text: String,
                onAccept: ()->Unit = { -> })
{
    val dialog = Dialog(context)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(true)
    dialog.setContentView(R.layout.dialog_ok)

    val textTitle = dialog.findViewById(R.id.textSimpleTitle) as TextView
    val textDesc = dialog.findViewById(R.id.textSimpleDesc) as TextView
    val butOk = dialog.findViewById(R.id.butSimpleOk)  as Button

    butOk.setOnClickListener {
        dialog.dismiss()
        onAccept()
    }

    textTitle.text = title
    textDesc.text = text

    dialog.show()
}
private fun showDialogXY(dialog: Dialog, x :Float? = null, y: Float? = null)
{
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