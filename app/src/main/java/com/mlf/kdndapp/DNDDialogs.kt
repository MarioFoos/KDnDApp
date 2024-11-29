package com.mlf.kdndapp

import android.app.Dialog
import android.content.Context
import android.text.method.ScrollingMovementMethod
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.dndlib.DNDWealth
import com.dndlib.base.ECoin
import com.dndlib.res.Res

fun dialogEditNum(context: Context, title: String, value: Int, onAccept: (newValue: Int)->Unit = { _ -> })
{
    var curLevel = value
    val dialog = Dialog(context)

    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(true)
    dialog.setContentView(R.layout.dialog_small_number)

    val butOk = dialog.findViewById(R.id.butOk)  as Button
    val butCancel = dialog.findViewById(R.id.butCancel)  as Button
    val textTitle = dialog.findViewById(R.id.textTitle) as TextView
    val textNumber = dialog.findViewById(R.id.textCurrent) as TextView
    val butInc = dialog.findViewById(R.id.butCurrentInc)  as Button
    val butDec = dialog.findViewById(R.id.butCurrentDec)  as Button

    butOk.text = Res.locale("but_ok")
    butCancel.text = Res.locale("but_cancel")
    textTitle.text = title
    textNumber.text = curLevel.toString()

    fun showData()
    {
        textNumber.text = curLevel.toString()
    }
    butInc.setOnClickListener {
        if(curLevel < 20)
        {
            ++curLevel
        }
        showData()
    }
    butDec.setOnClickListener {
        if(curLevel > 1)
        {
            --curLevel
        }
        showData()
    }
    butOk.setOnClickListener {
        dialog.cancel()
        onAccept(curLevel)
    }
    butCancel.setOnClickListener {
        dialog.dismiss()
    }
    showData()
    dialog.show()
}
fun dialogWealth(context: Context, wealth: Long, onAccept: (newWealth: Long)->Unit = { _ -> })
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
        dialog.cancel()
        onAccept(curWealth.total)
    }
    butCancel.setOnClickListener {
        dialog.dismiss()
    }
    showData()
    dialog.show()
}
fun dialogInfo(context: Context, title: String, desc: String? = null, x :Float? = null, y: Float? = null)
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
