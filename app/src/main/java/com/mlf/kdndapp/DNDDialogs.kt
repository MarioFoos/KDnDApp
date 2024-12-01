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
    val textTitle = dialog.findViewById(R.id.textXpTitle) as TextView
    val butOk = dialog.findViewById(R.id.butXpOk)  as Button
    val butCancel = dialog.findViewById(R.id.butXpCancel)  as Button

    textTitle.text = Res.locale("add_xp")
    butOk.text = Res.locale("but_add")
    butCancel.text = Res.locale("but_cancel")

    // Acciones
    val textXp = dialog.findViewById(R.id.textXpValue) as TextView
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
    val textTitle = dialog.findViewById(R.id.textHpTitle) as TextView
    val textDescCurrent = dialog.findViewById(R.id.textHpCurrentDesc) as TextView
    val textDescMax = dialog.findViewById(R.id.textHpMaxDesc) as TextView
    val textDescTemp = dialog.findViewById(R.id.textHpTempDesc) as TextView
    val butOk = dialog.findViewById(R.id.butHpOk)  as Button
    val butCancel = dialog.findViewById(R.id.butHpCancel)  as Button

    textTitle.text = Res.locale("hit_points")
    textDescCurrent.text = Res.locale("hp_currents")
    textDescMax.text = Res.locale("hp_max")
    textDescTemp.text = Res.locale("hp_temp")
    butOk.text = Res.locale("but_ok")
    butCancel.text = Res.locale("but_cancel")

    // Acciones
    val butCurrentDec = dialog.findViewById(R.id.butHpCurrentDec)  as PushButton
    val butCurrentInc = dialog.findViewById(R.id.butHpCurrentInc)  as PushButton
    val butMaxDec = dialog.findViewById(R.id.butHpMaxDec)  as PushButton
    val butMaxInc = dialog.findViewById(R.id.butHpMaxInc)  as PushButton
    val butTempDec = dialog.findViewById(R.id.butHpTempDec)  as PushButton
    val butTempInc = dialog.findViewById(R.id.butHpTempInc)  as PushButton
    val textCurrent = dialog.findViewById(R.id.textHpCurrent) as TextView
    val textMax = dialog.findViewById(R.id.textHpMax) as TextView
    val textTemp = dialog.findViewById(R.id.textHpTemp) as TextView

    fun showData()
    {
        textCurrent.text = curHp.current.toString()
        textMax.text = curHp.max.toString()
        textTemp.text = curHp.temp.toString()
    }
    fun changeHp(value: Int, delta: Int): Int
    {
        var res = value + delta
        if(res < 0)
        {
            res = 0
        }
        showData()
        return res
    }
    butCurrentDec.setAction {  curHp.current = changeHp(curHp.current, -1) }
    butCurrentInc.setAction {  curHp.current = changeHp(curHp.current, 1) }
    butMaxDec.setAction {  curHp.max = changeHp(curHp.max, -1) }
    butMaxInc.setAction {  curHp.max = changeHp(curHp.max, 1) }
    butTempDec.setAction {  curHp.temp = changeHp(curHp.temp, -1) }
    butTempInc.setAction {  curHp.temp = changeHp(curHp.temp, 1) }

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

    val butOk = dialog.findViewById(R.id.butNumOk)  as Button
    val butCancel = dialog.findViewById(R.id.butNumCancel)  as Button
    val textTitle = dialog.findViewById(R.id.textNumTitle) as TextView
    val textNumber = dialog.findViewById(R.id.textNumValue) as TextView
    val butInc = dialog.findViewById(R.id.butNumInc) as PushButton
    val butDec = dialog.findViewById(R.id.butNumDec) as PushButton

    butOk.text = Res.locale("but_ok")
    butCancel.text = Res.locale("but_cancel")
    textTitle.text = title
    textNumber.text = curValue.toString()

    fun showData()
    {
        textNumber.text = curValue.toString()
    }
    butInc.setAction {
        if(curValue < 20)
        {
            ++curValue
        }
        showData()
    }
    butDec.setAction {
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
fun dialogWealth2(context: Context, x: Float? = null, y: Float? = null,
                  onAdd: (wealth: DNDWealth)->Unit = { _ -> },
                  onSub: (wealth: DNDWealth)->Unit = { _ -> })
{
    val PLATINUM = 0
    val GOLD = 1
    val ELECTRUM = 2
    val SILVER = 3
    val CUPPER = 4
    var coins = arrayOf(0, 0, 0, 0, 0 )

    val dialog = Dialog(context)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(true)
    dialog.setContentView(R.layout.dialog_wealth_layout)

    val textTitle = dialog.findViewById(R.id.textCoinsTitle)  as TextView
    val butAdd = dialog.findViewById(R.id.butCoinsAdd)  as Button
    val butSub = dialog.findViewById(R.id.butCoinsSub)  as Button
    val butCancel = dialog.findViewById(R.id.butCoinsCancel2)  as Button

    textTitle.text = Res.locale("add_quit_coins")
    butAdd.text = Res.locale("but_add")
    butSub.text = Res.locale("but_sub")
    butCancel.text = Res.locale("but_cancel")

    val textPlatinum = dialog.findViewById(R.id.textCoinsPlatinum) as TextView
    val textGold = dialog.findViewById(R.id.textCoinsGold) as TextView
    val textElectrum = dialog.findViewById(R.id.textCoinsElectrum) as TextView
    val textSilver = dialog.findViewById(R.id.textCoinsSilver) as TextView
    val textCupper = dialog.findViewById(R.id.textCoinsCupper) as TextView

    fun showData()
    {
        textPlatinum.text = coins[PLATINUM].toString()
        textGold.text = coins[GOLD].toString()
        textElectrum.text = coins[ELECTRUM].toString()
        textSilver.text = coins[SILVER].toString()
        textCupper.text = coins[CUPPER].toString()
    }
    val butPlatinumInc1 = dialog.findViewById(R.id.butPlatinumInc1)  as PushButton
    val butPlatinumInc10 = dialog.findViewById(R.id.butPlatinumInc10)  as PushButton
    val butPlatinumDec1 = dialog.findViewById(R.id.butPlatinumDec1)  as PushButton
    val butPlatinumDec10 = dialog.findViewById(R.id.butPlatinumDec10)  as PushButton
    val butGoldInc1 = dialog.findViewById(R.id.butGoldInc1)  as PushButton
    val butGoldInc10 = dialog.findViewById(R.id.butGoldInc10)  as PushButton
    val butGoldDec1 = dialog.findViewById(R.id.butGoldDec1)  as PushButton
    val butGoldDec10 = dialog.findViewById(R.id.butGoldDec10)  as PushButton
    val butElectrumInc1 = dialog.findViewById(R.id.butElectrumInc1)  as PushButton
    val butElectrumInc10 = dialog.findViewById(R.id.butElectrumInc10)  as PushButton
    val butElectrumDec1 = dialog.findViewById(R.id.butElectrumDec1)  as PushButton
    val butElectrumDec10 = dialog.findViewById(R.id.butElectrumDec10)  as PushButton
    val butSilverInc1 = dialog.findViewById(R.id.butSilverInc1)  as PushButton
    val butSilverInc10 = dialog.findViewById(R.id.butSilverInc10)  as PushButton
    val butSilverDec1 = dialog.findViewById(R.id.butSilverDec1)  as PushButton
    val butSilverDec10 = dialog.findViewById(R.id.butSilverDec10)  as PushButton
    val butCupperInc1 = dialog.findViewById(R.id.butCupperInc1)  as PushButton
    val butCupperInc10 = dialog.findViewById(R.id.butCupperInc10)  as PushButton
    val butCupperDec1 = dialog.findViewById(R.id.butCupperDec1)  as PushButton
    val butCupperDec10 = dialog.findViewById(R.id.butCupperDec10)  as PushButton

    fun change(index: Int, value: Int)
    {
        coins[index] += value
        if(coins[index] < 0)
        {
            coins[index] = 0
        }
        showData()
    }
    butPlatinumInc1.setAction { change(PLATINUM, 1) }
    butPlatinumInc10.setAction { change(PLATINUM, 10) }
    butPlatinumDec1.setAction { change(PLATINUM, -1) }
    butPlatinumDec10.setAction { change(PLATINUM, -10) }
    butGoldInc1.setAction { change(GOLD, 1) }
    butGoldInc10.setAction { change(GOLD, 10) }
    butGoldDec1.setAction { change(GOLD, -1) }
    butGoldDec10.setAction { change(GOLD, -10) }
    butElectrumInc1.setAction { change(ELECTRUM, 1) }
    butElectrumInc10.setAction { change(ELECTRUM, 10) }
    butElectrumDec1.setAction { change(ELECTRUM, -1) }
    butElectrumDec10.setAction { change(ELECTRUM, -10) }
    butSilverInc1.setAction { change(SILVER, 1) }
    butSilverInc10.setAction { change(SILVER, 10) }
    butSilverDec1.setAction { change(SILVER, -1) }
    butSilverDec10.setAction { change(SILVER, -10) }
    butCupperInc1.setAction { change(CUPPER, 1) }
    butCupperInc10.setAction { change(CUPPER, 10) }
    butCupperDec1.setAction { change(CUPPER, -1) }
    butCupperDec10.setAction { change(CUPPER, -10) }

    fun buildWealth() : DNDWealth
    {
        var wealth = DNDWealth()
        wealth.add(ECoin.PLATINUM, coins[PLATINUM])
        wealth.add(ECoin.GOLD, coins[GOLD])
        wealth.add(ECoin.ELECTRUM, coins[ELECTRUM])
        wealth.add(ECoin.SILVER, coins[SILVER])
        wealth.add(ECoin.CUPPER, coins[CUPPER])
        return wealth
    }
    butAdd.setOnClickListener {
        dialog.dismiss()
        onAdd(buildWealth())
    }
    butSub.setOnClickListener {
        dialog.dismiss()
        onSub(buildWealth())
    }
    butCancel.setOnClickListener {
        dialog.dismiss()
    }
    showData()
    showDialogXY(dialog, x, y)
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
    val textDesc = dialog.findViewById(R.id.textInfoDesc) as TextView
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